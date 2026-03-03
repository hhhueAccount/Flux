package cn.zc.packet.clientbound.configuration

import cn.zc.extension.Identifier
import cn.zc.extension.readIdentifier
import cn.zc.extension.writeIdentifier
import cn.zc.packet.clientbound.ClientBoundPacket
import io.netty.buffer.ByteBuf

data class ClientCookieRequestPacket(val identifier: Identifier) : ClientBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(byteBuf.readIdentifier())

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeIdentifier(identifier)
    }
}
