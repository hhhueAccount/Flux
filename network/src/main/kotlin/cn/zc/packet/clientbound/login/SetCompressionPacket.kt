package cn.zc.packet.clientbound.login

import cn.zc.packet.clientbound.ClientBoundPacket
import cn.zc.serialize.VarInt
import kotlinx.serialization.Serializable

@Serializable
data class SetCompressionPacket(val threshold: VarInt) : ClientBoundPacket()