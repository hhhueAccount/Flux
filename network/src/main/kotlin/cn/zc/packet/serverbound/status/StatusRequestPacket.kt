package cn.zc.packet.serverbound.status

import cn.zc.packet.serverbound.ServerBoundPacket
import io.netty.buffer.ByteBuf

class StatusRequestPacket() : ServerBoundPacket() {
    constructor(byteBuf: ByteBuf) : this()

    override fun serialize(byteBuf: ByteBuf) {
    }
}
