package cn.zc.packet.clientbound.configuration

import cn.zc.packet.clientbound.ClientBoundPacket
import io.netty.buffer.ByteBuf

class ConfigDonePacket() : ClientBoundPacket() {
    constructor(byteBuf: ByteBuf) : this()

    override fun serialize(byteBuf: ByteBuf) {
    }
}
