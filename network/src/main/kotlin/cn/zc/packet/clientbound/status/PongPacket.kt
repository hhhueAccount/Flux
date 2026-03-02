package cn.zc.packet.clientbound.status

import cn.zc.packet.clientbound.ClientBoundPacket
import io.netty.buffer.ByteBuf

data class PongPacket(val timestamp: Long) : ClientBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(
        byteBuf.readLong()
    )

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeLong(timestamp)
    }
}
