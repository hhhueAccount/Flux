package cn.zc.packet.clientbound.login

import cn.zc.packet.clientbound.ClientBoundPacket
import cn.zc.serialize.PrefixedBytes
import kotlinx.serialization.Serializable

@Serializable
data class EncryptionRequestPaket(
    val serverId: String,
    val publicKey: PrefixedBytes,
    val verifyToken: PrefixedBytes,
    val shouldAuthenticate: Boolean
) : ClientBoundPacket()
