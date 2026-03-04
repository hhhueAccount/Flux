package cn.zc.registry

import cn.zc.packet.ErrorPacket
import cn.zc.packet.Packet
import io.netty.buffer.ByteBuf
import org.apache.logging.log4j.kotlin.logger
import org.jetbrains.annotations.ApiStatus
import kotlin.reflect.KClass

/**
 * 数据包注册表的抽象基类，管理数据包的类型与协议ID的映射关系。
 *
 * 此类是所有协议版本注册表的基类，主要功能包括：
 * 1. 为子类提供统一的注册表结构
 * 2. 根据数据包类型查找对应的协议ID
 * 3. 根据协议ID反序列化数据包
 *
 * 每个协议通讯阶段所对应的数据包注册表都需要继承此类并实现`packets`属性，
 * 以提供该版本协议中所有数据包的注册信息。
 *
 * @see PacketInfo 数据包信息封装类
 */
abstract class PacketRegistry {
    /**
     * 数据包信息列表，包含所有已注册的数据包。
     *
     * 子类必须重写此属性并提供完整的数据包列表。
     * 列表中的顺序决定了每个数据包的协议ID。
     *
     * 注意：子类应该使用不可变列表，并在声明时一次性初始化所有数据包信息。
     */
    abstract val packets: List<PacketInfo<*>>

    /**
     * 根据数据包类型获取对应的协议ID。
     *
     * 此方法用于在发送数据包时，确定需要写入网络流中的协议ID。
     * 它会遍历注册表中的所有数据包信息，找到匹配的类型并返回其索引。
     *
     * @param clazz 数据包的Kotlin类引用
     * @return 数据包在协议中的ID（即其在列表中的索引位置）
     * @throws IllegalStateException 如果指定的数据包类型未在注册表中注册
     */
    fun getId(clazz: KClass<*>): Int {
        for (info in packets) {
            if (clazz == info.clazz) {
                return packets.indexOf(info)
            }
        }
        // 找不到就报错
        logger.error("注册表 $this 中未注册类型为 ${clazz.simpleName} 的数据包，现在已经取消发送")
        return -2
    }

    /**
     * 根据协议ID反序列化数据包。
     *
     * 此方法用于从网络字节流中读取数据包。通过协议ID索引到对应的数据包信息，
     * 然后调用其反序列化函数将[ByteBuf]转换为具体的数据包对象。
     *
     * @param id 数据包在协议中的ID
     * @param byteBuf 包含数据包原始数据的Netty字节缓冲区
     * @return 反序列化后的数据包对象
     * 注意：需要确保ID在有效范围内（0 <= id < packets.size）。
     */
    @ApiStatus.Internal
    fun deserialize(id: Int, byteBuf: ByteBuf): Packet {
        if (packets.size <= id) {
            // 不存在就报错
            logger.error("注册表 $this 中未注册ID为 $id 的数据包")
            return ErrorPacket
        }
        return packets[id].deserializer(byteBuf)
    }
}