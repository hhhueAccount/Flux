package cn.zc.packet

import cn.zc.Session
import io.netty.buffer.ByteBuf

/**
 * 所有网络数据包的抽象基类，定义了数据包读取逻辑的标准接口。
 *
 * 考虑到工作量，我不会为任何一个子类，或者说实现，写注释。
 * 想要查阅具体信息，请参阅：[wiki.vg merge](https://minecraft.wiki/w/Java_Edition_protocol/Packets)
 */
abstract class Packet {
    /**
     * 标识这个数据包从哪里被发送过来。
     *
     * 这个值只有在入站数据包中才会被初始化。
     * - 在客户端网络模块，只有[cn.zc.packet.clientbound.ClientBoundPacket]才会初始化这个值
     * - 在服务端网络模块，只有[cn.zc.packet.serverbound.ServerBoundPacket]才会初始化这个值
     */
    lateinit var from: Session

    /**
     * 将数据包对象序列化到指定的字节缓冲区。
     *
     * 此方法由具体的数据包子类实现，负责将数据包的字段按照`Minecraft`协议格式。
     * 编码并写入到[byteBuf]中。序列化后的数据将通过网络发送到客户端或服务端。
     *
     * @param byteBuf 目标字节缓冲区，序列化后的数据将写入此缓冲区
     */
    abstract fun serialize(byteBuf: ByteBuf)
}