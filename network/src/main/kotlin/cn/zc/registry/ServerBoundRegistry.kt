package cn.zc.registry

import cn.zc.packet.PlaceHolerPacket
import cn.zc.packet.serverbound.common.PingPacket
import cn.zc.packet.serverbound.handshake.IntentionPacket
import cn.zc.packet.serverbound.login.LoginAcknowledgedPacket
import cn.zc.packet.serverbound.login.LoginStartPacket
import cn.zc.packet.serverbound.status.StatusRequestPacket
import kotlinx.serialization.ExperimentalSerializationApi
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
@ExperimentalSerializationApi
class ServerBoundRegistry {
    object Handshake : PacketRegistry() {
        init {
            register(IntentionPacket.serializer())
        }
    }

    object Status : PacketRegistry() {
        init {
            register(StatusRequestPacket.serializer())
            register(PingPacket.serializer())
        }
    }

    object Login : PacketRegistry() {
        init {
            register(LoginStartPacket.serializer())
            register(PlaceHolerPacket.serializer())
            register(LoginAcknowledgedPacket.serializer())
        }
    }

    object Configuration : PacketRegistry()
    object Play : PacketRegistry()
}