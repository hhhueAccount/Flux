package cn.zc.packet.clientbound.common

import cn.zc.packet.serverbound.ServerBoundPacket
import cn.zc.serialize.Identifier
import cn.zc.serialize.RawBytes
import kotlinx.serialization.Serializable

@Serializable
data class ClientBoundPluginMessagePacket(
    val channel: Identifier,
    val data: RawBytes
) : ServerBoundPacket()
