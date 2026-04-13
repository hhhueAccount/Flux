package cn.zc.packet.serverbound.handshake

import cn.zc.packet.serverbound.ServerBoundPacket
import cn.zc.serialize.VarInt
import kotlinx.serialization.Serializable

@Serializable
data class IntentionPacket(
    val protocolVer: VarInt,
    val serverAddress: String,
    val port: Short,
    val intention: Intention
) : ServerBoundPacket() {
    @Suppress("Unused")
    enum class Intention {
        NULL,
        STATUS,
        LOGIN,
        TRANSFER
    }
}