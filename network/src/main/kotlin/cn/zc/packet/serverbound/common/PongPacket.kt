package cn.zc.packet.serverbound.common

import cn.zc.packet.serverbound.ServerBoundPacket
import io.netty.buffer.ByteBuf

data class PongPacket(val id: Int) : ServerBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(byteBuf.readInt())

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeInt(id)
    }
}
