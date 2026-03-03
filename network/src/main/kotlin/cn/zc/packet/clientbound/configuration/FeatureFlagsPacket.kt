package cn.zc.packet.clientbound.configuration

import cn.zc.extension.*
import cn.zc.packet.clientbound.ClientBoundPacket
import io.netty.buffer.ByteBuf

data class FeatureFlagsPacket(val identifiers: List<Identifier>) : ClientBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(byteBuf.readList(ByteBuf::readIdentifier))

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeList(identifiers, ByteBuf::writeIdentifier)
    }
}
