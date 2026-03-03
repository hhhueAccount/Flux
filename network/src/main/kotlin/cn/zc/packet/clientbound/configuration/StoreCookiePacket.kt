package cn.zc.packet.clientbound.configuration

import cn.zc.extension.*
import cn.zc.packet.clientbound.ClientBoundPacket
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
