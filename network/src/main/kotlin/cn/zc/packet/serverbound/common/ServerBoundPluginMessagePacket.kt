package cn.zc.packet.serverbound.common

import cn.zc.packet.serverbound.ServerBoundPacket
import cn.zc.serialize.Identifier
import cn.zc.serialize.RawBytes
import kotlinx.serialization.Serializable

@Serializable
data class ServerBoundPluginMessagePacket(
    val channel: Identifier,
    val data: RawBytes
) : ServerBoundPacket()
