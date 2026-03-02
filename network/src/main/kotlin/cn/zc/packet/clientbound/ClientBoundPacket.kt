package cn.zc.packet.clientbound

import cn.zc.packet.Packet
import io.netty.buffer.ByteBuf

/**
 * 发往客户端的数据包
 */
abstract class ClientBoundPacket : Packet()