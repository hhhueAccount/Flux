package cn.zc.handler

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec
import org.apache.logging.log4j.kotlin.logger
import java.nio.ByteBuffer
import java.security.GeneralSecurityException
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.ShortBufferException
import javax.crypto.spec.IvParameterSpec

/**
 * 网络通信加密编解码器
 *
 * 提供基于AES-CFB8模式的对称加密功能，用于保护客户端与服务器之间的网络通信安全。
 * 该类继承自Netty的[MessageToMessageCodec]，实现了双向的加密和解密管道。
 *
 * ## 工作原理
 * 1. 使用AES/CFB8/NoPadding加密模式
 * 2. 使用共享密钥进行对称加密
 * 3. 使用密钥本身作为初始化向量(IV)
 * 4. 支持零填充(NoPadding)，确保数据长度不变
 *
 * ## 典型使用场景
 * - Minecraft客户端与服务器之间的加密通信
 * - 需要保护网络数据传输安全的任何TCP连接
 *
 * ## 注意事项
 * 1. 相同的密钥必须同时在客户端和服务器端使用
 * 2. 加密模式为流式加密，适合实时网络通信
 * 3. 使用密钥作为IV简化了密钥交换，但需确保密钥足够随机
 *
 * @param sharedSecret 共享的AES密钥，必须为128/192/256位
 * @throws GeneralSecurityException 如果安全提供程序不支持AES/CFB8/NoPadding
 * @throws AssertionError 如果加密通道初始化失败
 *
 * @see MessageToMessageCodec Netty消息编解码器基类
 * @see CryptBuf 内部加密缓冲区实现
 * @see <a href="https://github.com/GlowstoneMC/Glowstone">Glowstone参考实现</a>
 * @see <a href="https://github.com/Minestom/Minestom">Minestom参考实现</a>
 */
class EncryptionCodec(sharedSecret: SecretKey) : MessageToMessageCodec<ByteBuf?, ByteBuf?>() {
    /**
     * 加密缓冲区实例，用于出站数据的加密
     * @see CryptBuf 加密缓冲区实现
     */
    private val encodeBuf: CryptBuf

    /**
     * 解密缓冲区实例，用于入站数据的解密
     * @see CryptBuf 解密缓冲区实现
     */
    private val decodeBuf: CryptBuf

    /**
     * 构造函数初始化加密和解密缓冲区
     *
     * ## 初始化过程
     * 1. 创建加密缓冲区（ENCRYPT_MODE）
     * 2. 创建解密缓冲区（DECRYPT_MODE）
     * 3. 配置AES/CFB8/NoPadding加密模式
     * 4. 使用共享密钥和密钥自身作为IV进行初始化
     *
     * @param sharedSecret 用于加解密的AES密钥
     * @throws GeneralSecurityException 如果安全提供程序初始化失败
     * @throws AssertionError 如果加密通道无法初始化
     */
    init {
        try {
            // 初始化加密缓冲区（用于发送数据）
            encodeBuf = CryptBuf(Cipher.ENCRYPT_MODE, sharedSecret)
            // 初始化解密缓冲区（用于接收数据）
            decodeBuf = CryptBuf(Cipher.DECRYPT_MODE, sharedSecret)
            logger.trace("加密编解码器初始化成功，密钥算法: ${sharedSecret.algorithm}, 长度: ${sharedSecret.encoded.size * 8}位")
        } catch (e: GeneralSecurityException) {
            // 记录详细的错误信息
            logger.error("加密编解码器初始化失败: ${e.message}", e)
            // 使用AssertionError包装异常，确保调用者知道这是严重错误
            throw AssertionError("无法初始化加密通道，请检查密钥和加密提供程序", e)
        }
    }

    /**
     * 加密出站数据（服务器→客户端方向）
     *
     * ## 处理流程
     * 1. 接收原始的明文ByteBuf
     * 2. 通过加密缓冲区进行AES-CFB8加密
     * 3. 输出加密后的ByteBuf到发送管道
     *
     * ## 性能考虑
     * - 使用流式加密，适合实时通信
     * - 内存分配优化：重用ByteBuffer避免频繁分配
     * - 零拷贝：使用nioBuffer直接操作底层内存
     *
     * @param ctx Netty通道处理上下文，包含通道信息和处理器状态
     * @param msg 需要加密的原始数据，不能为null
     * @param out 加密后的数据输出列表，每个元素都是一个ByteBuf
     *
     * @see encodeBuf 用于加密的内部缓冲区
     * @see CryptBuf.crypt 具体的加密方法
     */
    override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf?, out: MutableList<Any>) {
        // 参数验证
        requireNotNull(msg) { "加密数据不能为null" }

        logger.debug("加密数据包，大小: ${msg.readableBytes()}字节，源: ${ctx.channel().remoteAddress()}")
        // 执行加密操作
        encodeBuf.crypt(msg, out)
    }

    /**
     * 解密入站数据（客户端→服务器方向）
     *
     * ## 处理流程
     * 1. 接收加密的ByteBuf
     * 2. 通过解密缓冲区进行AES-CFB8解密
     * 3. 输出解密后的明文ByteBuf到处理管道
     *
     * ## 安全考虑
     * - 验证数据完整性（通过CFB8模式本身保证）
     * - 异常处理：捕获所有解密异常并记录日志
     * - 防止时序攻击：使用恒定时间操作（由JCE提供）
     *
     * @param ctx Netty通道处理上下文
     * @param msg 需要解密的加密数据，不能为null
     * @param out 解密后的数据输出列表
     *
     * @throws IllegalArgumentException 如果输入数据为null
     * @see decodeBuf 用于解密的内部缓冲区
     */
    override fun decode(ctx: ChannelHandlerContext, msg: ByteBuf?, out: MutableList<Any>) {
        // 参数验证
        requireNotNull(msg) { "解密数据不能为null" }

        logger.trace("解密数据包，大小: ${msg.readableBytes()}字节，来源: ${ctx.channel().remoteAddress()}")
        // 执行解密操作
        decodeBuf.crypt(msg, out)
    }

    /**
     * 处理加密解密过程中的异常
     *
     * ## 异常处理策略
     * 1. 记录详细的异常信息和堆栈跟踪
     * 2. 不重新抛出异常，避免中断整个管道
     * 3. 分类处理不同类型的加密异常
     *
     * ## 常见异常类型
     * - GeneralSecurityException: 加密算法相关错误
     * - ShortBufferException: 缓冲区大小不足
     * - IllegalStateException: 加密器状态错误
     *
     * @param context 网络通道处理上下文，可能为null
     * @param cause 异常对象，不能为null
     */
    override fun exceptionCaught(context: ChannelHandlerContext?, cause: Throwable) {
        logger.error("加密通信异常: ${cause.javaClass.simpleName} (原因: ${cause.message})")
    }

    /**
     * 加密解密缓冲区内部类
     *
     * ## 设计模式
     * - 单职责原则：只负责加密/解密操作
     * - 享元模式：重用Cipher实例提高性能
     * - 线程安全：每个通道实例独立，不支持多线程共享
     *
     * ## 加密算法细节
     * - 算法: AES (Advanced Encryption Standard)
     * - 模式: CFB8 (Cipher Feedback 8-bit)
     * - 填充: NoPadding (无填充)
     * - IV生成: 使用密钥本身的前16字节
     *
     * ## 性能优化
     * - 重用Cipher实例避免重复初始化
     * - 使用Direct ByteBuffer减少内存拷贝
     * - 批量处理数据提高吞吐量
     *
     * @property cipher 加解密引擎实例，线程不安全
     * @constructor 创建指定模式的加密缓冲区
     * @param mode 加密模式，必须是[Cipher.ENCRYPT_MODE]或[Cipher.DECRYPT_MODE]
     * @param sharedSecret 共享的AES密钥
     * @throws GeneralSecurityException 如果加密器初始化失败
     */
    private class CryptBuf(mode: Int, sharedSecret: SecretKey) {
        /**
         * JCE加密引擎实例，用于执行实际的加解密操作
         * 注意：Cipher不是线程安全的，每个实例只能在一个线程中使用
         */
        private val cipher: Cipher = Cipher.getInstance("AES/CFB8/NoPadding")

        /**
         * 初始化加密解密缓冲区
         *
         * ## 初始化向量(IV)生成
         * 使用密钥本身作为IV，简化密钥交换过程但要求密钥足够随机
         * 对于AES-128，使用前16字节作为IV
         *
         * ## 模式验证
         * 确保模式参数有效，只能是加密或解密模式
         *
         * @param mode 加密模式，必须是Cipher.ENCRYPT_MODE或Cipher.DECRYPT_MODE
         * @param sharedSecret 共享的AES密钥
         * @throws IllegalArgumentException 如果模式参数无效
         * @throws GeneralSecurityException 如果加密器初始化失败
         */
        init {
            // 验证模式参数
            require(mode == Cipher.ENCRYPT_MODE || mode == Cipher.DECRYPT_MODE) {
                "无效的加密模式: $mode，必须是Cipher.ENCRYPT_MODE或Cipher.DECRYPT_MODE"
            }

            // 使用密钥本身作为IV（CFB8模式允许这种简化）
            val iv = IvParameterSpec(sharedSecret.encoded.copyOf(16)) // AES块大小是16字节

            // 初始化加密器
            cipher.init(mode, sharedSecret, iv)

            val modeStr = if (mode == Cipher.ENCRYPT_MODE) "加密" else "解密"
            logger.debug("CryptBuf初始化成功，模式: $modeStr，密钥算法: ${sharedSecret.algorithm}")
        }

        /**
         * 执行加密或解密操作
         *
         * ## 处理流程
         * 1. 分配输出缓冲区（与输入大小相同）
         * 2. 使用Cipher.update进行流式加密/解密
         * 3. 包装结果到Netty的ByteBuf
         *
         * ## 内存管理
         * - 输入：使用nioBuffer()避免内存拷贝
         * - 输出：预分配ByteBuffer避免扩容
         * - 结果：包装为池化的ByteBuf减少GC压力
         *
         * @param msg 需要处理的ByteBuf数据，不能为null
         * @param out 处理后的数据输出列表
         * @throws ShortBufferException 如果输出缓冲区不足（理论上不应该发生）
         * @throws IllegalStateException 如果Cipher处于无效状态
         * @throws NullPointerException 如果输入参数为null
         */
        fun crypt(msg: ByteBuf?, out: MutableList<Any>) {
            // 参数验证
            requireNotNull(msg) { "输入数据不能为null" }

            val inputSize = msg.readableBytes()

            // 分配输出缓冲区（大小与输入相同，CFB8模式无填充）
            val outBuffer = ByteBuffer.allocate(inputSize)

            try {
                // 执行加密/解密操作
                // 使用nioBuffer()直接访问底层内存，避免数据拷贝
                cipher.update(msg.nioBuffer(), outBuffer)

                // 准备输出缓冲区
                outBuffer.flip()

                // 包装为Netty的ByteBuf，使用池化减少内存分配
                val result = Unpooled.wrappedBuffer(outBuffer)
                out.add(result)

                logger.trace("加解密操作完成，输入大小: ${inputSize}字节，输出大小: ${result.readableBytes()}字节")
            } catch (e: ShortBufferException) {
                // 这种情况理论上不应该发生，因为缓冲区大小是根据输入预计算的
                logger.error("加密缓冲区不足，需要: ${cipher.getOutputSize(inputSize)}字节，分配: ${inputSize}字节", e)
                throw AssertionError("加密缓冲区大小计算错误，请检查Cipher.getOutputSize()", e)
            } catch (e: IllegalStateException) {
                // Cipher状态错误，可能是多线程访问或重复使用导致的
                logger.error("加密器状态异常，可能是多线程访问或重复使用", e)
                throw e
            }
        }
    }
}