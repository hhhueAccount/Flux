package cn.zc.packet.clientbound.configuration

import cn.zc.extension.writeOptional
import cn.zc.extension.writeTextComponent
import cn.zc.extension.writeUTF8
import cn.zc.extension.writeUuid
import cn.zc.packet.clientbound.ClientBoundPacket
import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import java.util.*

data class AddResourcePackPacket(
    val uuid: UUID,
    val url: String,
    val hash: String,
    val forced: Boolean,
    val prompt: Component?
) : ClientBoundPacket() {
    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeUuid(uuid)
        byteBuf.writeUTF8(url)
        byteBuf.writeUTF8(hash)
        byteBuf.writeBoolean(forced)
        byteBuf.writeOptional(prompt, ByteBuf::writeTextComponent)
    }
}
