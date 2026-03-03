package cn.zc.packet.clientbound.configuration

import cn.zc.extension.*
import cn.zc.packet.clientbound.ClientBoundPacket
import io.netty.buffer.ByteBuf
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
data class ClientPluginMessagePacket(val identifier: Identifier, val payload: ByteArray) : ClientBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(
        byteBuf.readIdentifier(),
        byteBuf.readByteArray()
    )

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeIdentifier(identifier)
        byteBuf.writeByteArray(payload)
    }

    override fun toString() = "PluginMessagePacket(identifier=$identifier, payload=${String(payload)})"
}
