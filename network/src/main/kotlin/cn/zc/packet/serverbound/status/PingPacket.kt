package cn.zc.packet.serverbound.status

import cn.zc.packet.serverbound.ServerBoundPacket
import io.netty.buffer.ByteBuf

data class PingPacket(val timestamp: Long) : ServerBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(
        byteBuf.readLong()
    )

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeLong(timestamp)
    }
}
