package cn.zc.packet.serverbound.common

import cn.zc.packet.serverbound.ServerBoundPacket
import kotlinx.serialization.Serializable

@Serializable
data class PingPacket(val timestamp: Long) : ServerBoundPacket()