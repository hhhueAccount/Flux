package cn.zc.packet.clientbound.status

import cn.zc.packet.clientbound.ClientBoundPacket
import cn.zc.serialize.Json
import kotlinx.serialization.Serializable

@Serializable
data class StatusResponsePacket(val json: Json) : ClientBoundPacket()