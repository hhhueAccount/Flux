package cn.zc.packet.clientbound.configuration

import cn.zc.extension.readByteArray
import cn.zc.extension.readIdentifier
import cn.zc.extension.writeByteArray
import cn.zc.extension.writeIdentifier
import cn.zc.packet.clientbound.ClientBoundPacket
import cn.zc.resource.Identifier
import io.netty.buffer.ByteBuf

data class StoreCookiePacket(
    val identifier: Identifier,
    val payload: ByteArray
) : ClientBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(
        byteBuf.readIdentifier(),
        byteBuf.readByteArray()
    )

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeIdentifier(identifier)
        byteBuf.writeByteArray(payload)
    }
}
