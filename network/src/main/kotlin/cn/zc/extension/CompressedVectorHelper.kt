package cn.zc.extension

import cn.zc.extension.CompressedVectorHelper.read
import cn.zc.extension.CompressedVectorHelper.write
import io.netty.buffer.ByteBuf
import org.bukkit.util.Vector
import kotlin.math.*

/**
 * 严格来说，这是一个工具类，所以名字中带有`Helper`
 *
 * 这个工具类暴露了两个方法，分别是[read]和[write]，
 * 功能分别是读取和写入经过特殊算法压缩的三维向量
 *
 * 所有算法来自于`Minecraft`原版通讯协议，内容未做修改
 */
object CompressedVectorHelper {

    /**
     * [ByteBuf]读取压缩的三维向量
     *
     * @param byteBuf 包含压缩向量数据的[ByteBuf]
     * @return 解码后的三维向量实例
     */
    fun read(byteBuf: ByteBuf): Vector {
        // 以下注释均为AI生成
        // 这段代码太晦涩了，我认为Mojang做了过度优化
        // 我把注释放这里了；所以，谁有兴趣看谁就看吧...

        // 读取第一个字节，如果为0表示零向量
        val first = byteBuf.readUnsignedByte().toLong()
        if (first == 0L) return Vector(0, 0, 0)

        // 读取第二个字节（数据次8位）
        val second = byteBuf.readUnsignedByte().toLong()
        // 读取第3-4字节（数据高16位）
        val thirdAndForth = byteBuf.readUnsignedInt()

        // 组合完整数据：thirdAndForth左移16位，second左移8位，first放在最低位
        // 形成48位数据：高16位来自thirdAndForth，中间8位来自second，低8位来自first
        val packedData = (thirdAndForth shl 16) or
                (second shl 8) or
                first
        // 提取缩放因子低2位（bits 0-1）
        var scaleFactor = first and 0b11L

        // 检查是否需要扩展缩放因子（bit 2为1表示需要额外读取）
        if (first and 0b100L == 0b100L) {
            // 读取额外的缩放因子数据（VarInt格式）
            // 将读取的32位值与已有的2位组合，形成完整的缩放因子
            scaleFactor = scaleFactor or
                    (byteBuf.readVarInt().toLong() and 0xFFFFFFFFL) shl 2
        }

        // 从打包数据中提取三个15位的坐标分量，解压并乘以缩放因子
        return Vector(
            // bits 3-17: X分量（右移3位取低15位）
            unpack(packedData shr 3) * scaleFactor,
            // bits 18-32: Y分量（右移18位取低15位）
            unpack(packedData shr 18) * scaleFactor,
            unpack(packedData shr 33) * scaleFactor
        )
    }

    /**
     * 解压单个坐标分量，将压缩的整数表示还原为[-1, 1]范围内的浮点数
     *
     * @param packed 压缩的坐标分量数据（低15位有效）
     * @return 归一化后的坐标值，范围在-1.0到1.0之间
     */
    private fun unpack(packed: Long) =
        min((packed and 32767L).toDouble(), 32766.0) * 2.0 / 32766.0 - 1.0

    /**
     * 将三维向量压缩并写入字节缓冲区
     *
     * @param buf 目标字节缓冲区，压缩数据将被写入
     * @param vec 待压缩的三维向量
     *
     * 代码实现来自 MC Protocol Lib（GeyserMC项目）
     */
    fun write(buf: ByteBuf, vec: Vector) {
        // 限制坐标范围，防止溢出
        val sanitizedX = sanitize(vec.getX())
        val sanitizedY = sanitize(vec.getY())
        val sanitizedZ = sanitize(vec.getZ())

        // 找到三个坐标分量的最大绝对值，用于确定缩放因子
        val maxVal = max(abs(sanitizedX), max(abs(sanitizedY), abs(sanitizedZ)))
        // 如果最大值非常小，直接写入0并返回（零向量）
        if (maxVal < 3.051944088384301E-5) {
            buf.writeByte(0)
            return
        }

        // 计算缩放因子：向上取整的最小整数，使得所有分量除以scale后在[-1, 1]范围内
        val scale = ceil(maxVal).toLong()
        // 检查缩放因子是否需要额外字节存储（scale的低2位是否等于scale本身）
        val scaleTooLargeForBits = (scale and 3L) != scale
        // 缩放因子的低2位（bits 0-1），如果过大则设置标志位（bit 2为1）
        val scaleBits = if (scaleTooLargeForBits) scale and 3L or 4L else scale

        // 归一化坐标并压缩为15位整数，然后放置到正确的位置
        val encodedX = pack(sanitizedX / scale) shl 3
        val encodedY = pack(sanitizedY / scale) shl 18
        val encodedZ = pack(sanitizedZ / scale) shl 33

        val packed = scaleBits or encodedX or encodedY or encodedZ

        // 低8位
        buf.writeByte(packed.toByte().toInt())
        // 次8位
        buf.writeByte((packed shr 8).toByte().toInt())
        // 高32位
        buf.writeInt((packed shr 16).toInt())

        // 如果缩放因子需要额外存储，写入扩展部分
        if (scaleTooLargeForBits) {
            buf.writeVarInt((scale shr 2).toInt())
        }
    }

    /**
     * 限制坐标值的有效范围，防止溢出和精度问题
     *
     * @param d 原始坐标值
     * @return 限制在安全范围内的坐标值
     */
    private fun sanitize(d: Double) =
        if (d < -1.7179869183E10) {
            -1.7179869183E10
        } else if (d > 1.7179869183E10) {
            1.7179869183E10
        } else {
            d
        }

    /**
     * 将归一化坐标值压缩为整数表示
     *
     * @param d 归一化后的坐标值，范围应在`-1`到`1`之间
     * @return 压缩后的15位整数表示（结果在`0`与`32766`之间）
     */
    private fun pack(d: Double) = ((d * 0.5 + 0.5) * 32766.0).roundToLong()
}