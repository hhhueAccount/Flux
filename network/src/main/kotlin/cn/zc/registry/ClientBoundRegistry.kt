package cn.zc.registry

import cn.zc.packet.clientbound.common.DisconnectPacket
import cn.zc.packet.clientbound.configuration.ClientCookieRequestPacket
import cn.zc.packet.clientbound.configuration.ClientPluginMessagePacket
import cn.zc.packet.clientbound.configuration.ConfigDonePacket
import cn.zc.packet.clientbound.login.EncryptionRequestPacket
import cn.zc.packet.clientbound.login.LoginFinishPacket
import cn.zc.packet.clientbound.login.SetCompressionPacket
import cn.zc.packet.clientbound.status.PongPacket
import cn.zc.packet.clientbound.status.StatusResponsePacket
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
class ClientBoundRegistry {
    object Handshake : Registry() {
        override val packets: List<PacketInfo<*>> = emptyList()
    }

    object Status : Registry() {
        override val packets: List<PacketInfo<*>> = listOf(
            PacketInfo(StatusResponsePacket::class, ::StatusResponsePacket),
            PacketInfo(PongPacket::class, ::PongPacket),
        )
    }

    object Login : Registry() {
        override val packets: List<PacketInfo<*>> = listOf(
            PacketInfo(DisconnectPacket::class, ::DisconnectPacket),
            PacketInfo(EncryptionRequestPacket::class, ::EncryptionRequestPacket),
            PacketInfo(LoginFinishPacket::class, ::LoginFinishPacket),
            PacketInfo(SetCompressionPacket::class, ::SetCompressionPacket)
            // plugin 通讯、Cookie由于原版内核放弃了相关逻辑的实现，故此处同样不实现
        )
    }

    object Configuration : Registry() {
        override val packets: List<PacketInfo<*>> = listOf(
            PacketInfo(ClientCookieRequestPacket::class, ::ClientCookieRequestPacket),
            PacketInfo(ClientPluginMessagePacket::class, ::ClientPluginMessagePacket),
            PacketInfo(DisconnectPacket::class, ::DisconnectPacket),
            PacketInfo(ConfigDonePacket::class, ::ConfigDonePacket)
        )
    }

    object Play : Registry() {
        override val packets: List<PacketInfo<*>> = listOf(

        )
    }
}