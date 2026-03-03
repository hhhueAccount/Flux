package cn.zc.registry

import cn.zc.packet.serverbound.configuration.ClientInformationPacket
import cn.zc.packet.serverbound.configuration.ServerPluginMessagePacket
import cn.zc.packet.serverbound.handshake.IntentionPacket
import cn.zc.packet.serverbound.login.EncryptionResponsePacket
import cn.zc.packet.serverbound.login.LoginKnownPacket
import cn.zc.packet.serverbound.login.LoginStartPacket
import cn.zc.packet.serverbound.status.PingPacket
import cn.zc.packet.serverbound.status.StatusRequestPacket
import org.jetbrains.annotations.ApiStatus

/**
 * 服务端数据包注册表
 */
@ApiStatus.Internal
class ServerBoundRegistry {
    object Handshake : Registry() {
        override val packets: List<PacketInfo<*>> = listOf(
            PacketInfo(IntentionPacket::class, ::IntentionPacket)
        )
    }

    object Status : Registry() {
        override val packets: List<PacketInfo<*>> = listOf(
            PacketInfo(StatusRequestPacket::class, ::StatusRequestPacket),
            PacketInfo(PingPacket::class, ::PingPacket),
        )
    }

    object Login : Registry() {
        override val packets: List<PacketInfo<*>> = listOf(
            PacketInfo(LoginStartPacket::class, ::LoginStartPacket),
            PacketInfo(EncryptionResponsePacket::class, ::EncryptionResponsePacket),
            PacketInfo.PLACEHOLDER,
            PacketInfo(LoginKnownPacket::class, ::LoginKnownPacket)
        )
    }

    object Configuration : Registry() {
        override val packets: List<PacketInfo<*>> = listOf(
            PacketInfo(ClientInformationPacket::class, ::ClientInformationPacket),
            PacketInfo.PLACEHOLDER,
            PacketInfo(ServerPluginMessagePacket::class, ::ServerPluginMessagePacket)
        )
    }


    object Play : Registry() {
        override val packets: List<PacketInfo<*>> = listOf(

        )
    }
}