package cn.zc.packet.serverbound

import cn.zc.packet.Packet
import io.netty.buffer.ByteBuf

/**
 * 发往服务端的数据包
 */
abstract class ServerBoundPacket : Packet()