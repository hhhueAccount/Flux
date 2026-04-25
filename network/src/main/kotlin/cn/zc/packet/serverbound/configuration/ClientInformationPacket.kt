package cn.zc.packet.serverbound.configuration

import cn.zc.packet.serverbound.ServerBoundPacket
import cn.zc.serialize.VarInt
import kotlinx.serialization.Serializable

@Serializable
data class ClientInformationPacket(
    val locale: String,
    val viewDistance: Byte,
    val chatMode: ChatMode,
    val chatColors: Boolean,
    val displayedSkin: Byte,
    val mainHand: MainHand,
    val enableTextFilter: Boolean,
    val allowServerListing: Boolean,
    val particleStatus: ParticleStatus
) : ServerBoundPacket() {
    enum class ChatMode {
        ENABLED,
        COMMANDS_ONLY,
        HIDDEN
    }

    enum class MainHand {
        LEFT,
        RIGHT
    }

    enum class ParticleStatus {
        ALL,
        DECREASED,
        MINIMAL
    }
}
