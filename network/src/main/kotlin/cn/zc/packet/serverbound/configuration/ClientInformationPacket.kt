package cn.zc.packet.serverbound.configuration

import cn.zc.extension.readUTF8
import cn.zc.extension.readVarInt
import cn.zc.extension.writeUTF8
import cn.zc.extension.writeVarInt
import cn.zc.packet.serverbound.ServerBoundPacket
import io.netty.buffer.ByteBuf
import java.util.*

data class ClientInformationPacket(
    val playerLocale: Locale,
    val viewDistance: Int,
    val chatMode: Int,
    val enableChatColor: Boolean,
    val displayedSkinParts: Int,
    val mainHand: Int,
    val enableTextFiltering: Boolean,
    val allowServerListing: Boolean,
    val particleStatus: Int
) : ServerBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(
        parse(byteBuf.readUTF8()),
        byteBuf.readByte().toInt(),
        byteBuf.readVarInt(),
        byteBuf.readBoolean(),
        byteBuf.readByte().toInt(),
        byteBuf.readVarInt(),
        byteBuf.readBoolean(),
        byteBuf.readBoolean(),
        byteBuf.readVarInt()
    )

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeUTF8(playerLocale.toString())
        byteBuf.writeByte(viewDistance)
        byteBuf.writeVarInt(chatMode)
        byteBuf.writeBoolean(enableChatColor)
        byteBuf.writeByte(displayedSkinParts)
        byteBuf.writeVarInt(mainHand)
        byteBuf.writeBoolean(enableTextFiltering)
        byteBuf.writeBoolean(allowServerListing)
        byteBuf.writeVarInt(particleStatus)
    }

    private companion object {
        private fun parse(localeString: String): Locale {
            val normalized = localeString.replace('_', '-')
            return Locale.forLanguageTag(normalized)
        }
    }
}
