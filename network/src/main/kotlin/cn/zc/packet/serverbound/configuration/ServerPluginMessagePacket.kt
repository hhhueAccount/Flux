package cn.zc.packet.serverbound.configuration

import cn.zc.extension.Identifier
import cn.zc.extension.readByteArray
import cn.zc.extension.readIdentifier
import cn.zc.extension.writeByteArray
import cn.zc.extension.writeIdentifier
import cn.zc.packet.serverbound.ServerBoundPacket
import io.netty.buffer.ByteBuf
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
data class ServerPluginMessagePacket(val identifier: Identifier, val payload: ByteArray) : ServerBoundPacket() {
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
