package cn.zc.packet.clientbound.configuration

import cn.zc.extension.readIdentifier
import cn.zc.extension.readList
import cn.zc.extension.writeIdentifier
import cn.zc.extension.writeList
import cn.zc.packet.clientbound.ClientBoundPacket
import cn.zc.resource.Identifier
import io.netty.buffer.ByteBuf

data class FeatureFlagsPacket(val identifiers: List<Identifier>) : ClientBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(byteBuf.readList(ByteBuf::readIdentifier))

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeList(identifiers, ByteBuf::writeIdentifier)
    }
}
