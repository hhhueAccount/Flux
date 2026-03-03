package cn.zc.packet.clientbound.common

import cn.zc.packet.clientbound.ClientBoundPacket
import io.netty.buffer.ByteBuf

data class ClientKeepAlivePacket(val id: Long) : ClientBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(byteBuf.readLong())

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeLong(id)
    }
}
