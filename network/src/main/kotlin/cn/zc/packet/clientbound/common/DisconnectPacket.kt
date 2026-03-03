package cn.zc.packet.clientbound.common

import cn.zc.extension.readUTF8
import cn.zc.extension.writeUTF8
import cn.zc.packet.clientbound.ClientBoundPacket
import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer

data class DisconnectPacket(val reason: Component) : ClientBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(
        JSONComponentSerializer.json().deserialize(byteBuf.readUTF8())
    )

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeUTF8(JSONComponentSerializer.json().serialize(reason))
    }
}
