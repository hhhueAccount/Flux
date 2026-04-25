package cn.zc.packet.serverbound.login

import cn.zc.packet.serverbound.ServerBoundPacket
import cn.zc.serialize.NetworkUUID
import kotlinx.serialization.Serializable

@Serializable
data class LoginStartPacket(
    val playerName: String,
    val playerUuid: NetworkUUID
) : ServerBoundPacket()
