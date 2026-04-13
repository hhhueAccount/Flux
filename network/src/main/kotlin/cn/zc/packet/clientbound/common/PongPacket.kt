package cn.zc.packet.clientbound.common

import cn.zc.packet.clientbound.ClientBoundPacket
import kotlinx.serialization.Serializable

@Serializable
data class PongPacket(val timestamp: Long) : ClientBoundPacket()