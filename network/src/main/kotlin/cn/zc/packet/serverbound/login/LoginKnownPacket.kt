package cn.zc.packet.serverbound.login

import cn.zc.packet.serverbound.ServerBoundPacket
import io.netty.buffer.ByteBuf

class LoginKnownPacket() : ServerBoundPacket() {
    constructor(byteBuf: ByteBuf) : this()

    override fun serialize(byteBuf: ByteBuf) {
    }
}
