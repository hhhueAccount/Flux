package cn.zc.packet.clientbound.common

import cn.zc.packet.clientbound.ClientBoundPacket
import cn.zc.serialize.JsonTextComponent
import kotlinx.serialization.Serializable

@Serializable
data class DisconnectPacket(val reason: JsonTextComponent) : ClientBoundPacket()
