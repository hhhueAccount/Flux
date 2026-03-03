package cn.zc.registry

import cn.zc.packet.Packet
import cn.zc.packet.PlaceHolerPacket
import io.netty.buffer.ByteBuf
import org.jetbrains.annotations.ApiStatus
import kotlin.reflect.KClass

/**
 * 数据包信息类，用于存储数据包的信息和反序列化逻辑。
 *
 * @param T 数据包类型，必须是[Packet]的子类
 * @property clazz 数据包的Kotlin类引用，用于类型识别和反射操作
 * @property deserializer 数据包的反序列化函数，从[ByteBuf]读取数据并构建[T]类型对象
 *
 * @see Packet 数据包基类
 * @see ByteBuf Netty的字节缓冲区
 * @see KClass Kotlin类引用
 */
@ApiStatus.Internal
data class PacketInfo<T : Packet>(
    /**
     * 数据包的Kotlin类引用。
     */
    val clazz: KClass<T>,

    /**
     * 数据包的反序列化函数。
     *
     * 这个函数负责从网络字节流中解析数据包，它：
     * 1. 从[ByteBuf]读取字节数据
     * 2. 根据协议格式解析字段
     * 3. 构建并返回[T]类型的数据包对象
     */
    val deserializer: (ByteBuf) -> T
) {
    companion object {
        val PLACEHOLDER: PacketInfo<PlaceHolerPacket> = PacketInfo(PlaceHolerPacket::class, ::PlaceHolerPacket)
    }
}