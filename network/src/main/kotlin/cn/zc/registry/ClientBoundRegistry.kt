package cn.zc.registry

import cn.zc.packet.PlaceHolerPacket
import cn.zc.packet.clientbound.common.ClientBoundPluginMessagePacket
import cn.zc.packet.clientbound.common.DisconnectPacket
import cn.zc.packet.clientbound.common.PongPacket
import cn.zc.packet.clientbound.configuration.FinishConfigurationPacket
import cn.zc.packet.clientbound.configuration.ResetChatPacket
import cn.zc.packet.clientbound.login.EncryptionRequestPaket
import cn.zc.packet.clientbound.login.LoginSuccessPacket
import cn.zc.packet.clientbound.login.SetCompressionPacket
import cn.zc.packet.clientbound.status.StatusResponsePacket
import cn.zc.packet.serverbound.common.PingPacket
import kotlinx.serialization.ExperimentalSerializationApi
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
@ExperimentalSerializationApi
class ClientBoundRegistry {
    object Handshake : PacketRegistry()

    object Status : PacketRegistry() {
        init {
            register(StatusResponsePacket.serializer())
            register(PongPacket.serializer())
        }
    }

    object Login : PacketRegistry() {
        init {
            register(DisconnectPacket.serializer())
            register(EncryptionRequestPaket.serializer())
            register(LoginSuccessPacket.serializer())
            register(SetCompressionPacket.serializer())
            // 后面的两个数据包官方没有给出处理逻辑，故放弃实现
        }
    }

    object Configuration : PacketRegistry() {
        init {
            register(PlaceHolerPacket.serializer())
            register(ClientBoundPluginMessagePacket.serializer())
            register(DisconnectPacket.serializer())
            register(FinishConfigurationPacket.serializer())
            register(PlaceHolerPacket.serializer())
            register(PingPacket.serializer())
            register(ResetChatPacket.serializer())

        }
    }

    object Play : PacketRegistry()
}