package cn.zc.packet.clientbound.common

import cn.zc.packet.clientbound.ClientBoundPacket
import io.netty.buffer.ByteBuf

data class PingPacket(val id: Int) : ClientBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(byteBuf.readInt())

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeInt(id)
    }
}
