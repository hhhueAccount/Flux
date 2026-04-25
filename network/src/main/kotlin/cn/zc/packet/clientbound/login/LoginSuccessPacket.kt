package cn.zc.packet.clientbound.login

import cn.zc.packet.clientbound.ClientBoundPacket
import cn.zc.serialize.ListSerializer
import cn.zc.serialize.NetworkUUID
import cn.zc.serialize.OptionalSerializer
import com.google.common.base.Optional
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@ExperimentalSerializationApi
data class LoginSuccessPacket(
    val uuid: NetworkUUID,
    val playerName: String,
    @Serializable(with = ListSerializer::class)
    val properties: List<Property>
) : ClientBoundPacket() {
    @Serializable
    data class Property(
        val name: String,
        val value: String,
        @Serializable(with = OptionalSerializer::class)
        val signature: Optional<String>
    )
}
