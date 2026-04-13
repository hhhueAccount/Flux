package cn.zc.packet.clientbound.common

import cn.zc.packet.clientbound.ClientBoundPacket
import cn.zc.serialize.JsonComponent
import kotlinx.serialization.Serializable

@Serializable
data class DisconnectPacket(val reason: JsonComponent) : ClientBoundPacket()
