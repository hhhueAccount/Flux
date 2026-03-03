package cn.zc

import cn.zc.Session.Companion.get
import cn.zc.handler.CompressionHandler
import cn.zc.handler.EncryptionCodec
import cn.zc.packet.Packet
import com.google.common.base.MoreObjects
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import org.apache.logging.log4j.kotlin.logger
import org.jetbrains.annotations.ApiStatus
import javax.crypto.SecretKey

/**
 * 会话类，对[Channel]进行了一些功能封装和拓展
 *
 * 该类负责管理Minecraft客户端连接的状态、加密、压缩等配置。
 * 使用单例模式确保每个[Channel]只对应一个[Session]实例。
 *
 * @property state 当前连接状态，详见[ConnectionState]
 * @property channel Netty通道对象，用于实际的网络通信
 */
class Session {
    /**
     * 当前连接状态，控制协议处理和消息流转，初始状态为[ConnectionState.HANDSHAKE]，
     * 通过[nextState]方法推进状态
     *
     * 出于设计考虑，不允许非法的`set`操作，因为这是出于设计目的之外的
     */
    var state: ConnectionState
        private set

    /**
     * Netty通道对象，代表与客户端的网络连接
     *
     * 通过该通道进行数据的读写和管道配置
     */
    val channel: Channel

    /**
     * 私有构造函数，确保只能通过伴生对象的[get]方法创建实例
     *
     * @param channel 客户端网络通道
     */
    private constructor(channel: Channel) {
        this.channel = channel
        this.state = ConnectionState.HANDSHAKE
        map[channel] = this
    }

    /**
     * 推进连接状态到下一个阶段。
     *
     * 典型的Minecraft连接状态流转：`HANDSHAKE → STATUS → LOGIN → PLAY`
     * 如果推进到`PLAY`阶段再尝试推进，那么连接始终停留于`PLAY`阶段
     *
     * 每个状态对应不同的协议处理逻辑。
     *
     * @see ConnectionState.next 获取下一个状态
     */
    @ApiStatus.Internal
    fun nextState() {
        state = state.next()
        logger.trace("[NextState](${channel.remoteAddress()}) $state")
    }

    /**
     * 断开这个连接
     */
    fun disconnect(): ChannelFuture? =
        channel.close().syncUninterruptibly()

    /**
     * 异步发送数据包
     */
    fun send(packet: Packet): ChannelFuture? =
        channel.writeAndFlush(packet)

    /**
     * 同步发送数据包
     */
    fun sendSync(packet: Packet): ChannelFuture? =
        channel.writeAndFlush(packet).sync()

    /**
     * 异步发送多个数据包
     *
     * 先进行写入，然后再统一flush
     */
    fun send(vararg packets: Packet) {
        for (packet in packets) {
            channel.write(packet)
        }
        channel.flush()
    }

    /**
     * 同步发送多个数据包
     *
     * 先进行写入，然后再统一flush
     */
    fun sendSync(vararg packets: Packet) {
        for (packet in packets) {
            channel.write(packet).sync()
        }
        channel.flush()
    }

    /**
     * 启用数据加密，替换Netty管道中的加密处理器。
     *
     * 在Minecraft协议中，加密通常在登录阶段启用，使用共享密钥进行对称加密。
     * 替换掉管道中名为"encryption"的处理器（通常是空处理器）。
     *
     * @param sharedSecretKey 客户端和服务器协商的共享密钥
     * @see EncryptionCodec 加密编解码器实现
     */
    @ApiStatus.Internal
    fun encrypt(sharedSecretKey: SecretKey) {
        channel.pipeline()
            .replace(
                "encryption", "encryption",
                EncryptionCodec(sharedSecretKey)
            )
    }

    /**
     * 启用数据压缩，替换Netty管道中的压缩处理器。
     *
     * 根据Minecraft协议，当数据包大小超过阈值时会进行压缩。
     * 替换掉管道中名为"compression"的处理器（通常是空处理器）。
     *
     * @param threshold 压缩阈值，单位：字节。当数据包大小超过此值时启用压缩
     * @see CompressionHandler 压缩处理器实现
     */
    @ApiStatus.Internal
    fun compress(threshold: Int) {
        channel.pipeline()
            .replace(
                "compression", "compression",
                CompressionHandler(threshold)
            )
    }

    override fun toString() =
        MoreObjects.toStringHelper(this)
            .add("channel", channel)
            .add("state", state)
            .omitNullValues()
            .omitEmptyValues()
            .toString()

    /**
     * 伴生对象，提供[Session]实例的全局管理和访问
     */
    companion object {
        /**
         * [Channel]到[Session]的映射表，用于全局会话管理
         */
        private val map: MutableMap<Channel, Session> = HashMap()

        /**
         * 获取或创建与指定[Channel]关联的[Session]实例
         *
         * 这是[Session]类的工厂方法，确保每个通道只有一个会话实例
         * 如果映射表中已存在该通道的会话，则直接返回；否则创建新的会话并注册
         *
         * 注意：调用时需要保证线程安全
         *
         * @param channel 客户端网络通道
         * @return 与该通道关联的会话实例
         */
        @ApiStatus.Internal
        fun get(channel: Channel): Session =
            if (map.containsKey(channel)) {
                map[channel]!!
            } else {
                Session(channel)
            }
    }
}