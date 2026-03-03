package cn.zc.packet.serverbound.common

import cn.zc.packet.serverbound.ServerBoundPacket
import io.netty.buffer.ByteBuf

data class ServerKeepAlivePacket(val id: Long) : ServerBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(byteBuf.readLong())

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeLong(id)
    }
}
