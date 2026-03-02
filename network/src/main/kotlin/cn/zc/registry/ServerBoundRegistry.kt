package cn.zc.registry

import cn.zc.packet.serverbound.handshake.IntentionPacket
import cn.zc.packet.serverbound.status.PingPacket
import cn.zc.packet.serverbound.status.StatusRequestPacket

/**
 * 服务端数据包注册表
 */
class ServerBoundRegistry {
    object Handshake: Registry() {
        override val packets: List<PacketInfo<*>> = listOf(
            PacketInfo(IntentionPacket::class, ::IntentionPacket)
        )
    }

    object Status: Registry() {
        override val packets: List<PacketInfo<*>> = listOf(
            PacketInfo(StatusRequestPacket::class, ::StatusRequestPacket),
            PacketInfo(PingPacket::class, ::PingPacket),
        )
    }

    object Login: Registry() {
        override val packets: List<PacketInfo<*>> = listOf(

        )
    }

    object Configuration: Registry() {
        override val packets: List<PacketInfo<*>> = listOf(

        )
    }


    object Play: Registry() {
        override val packets: List<PacketInfo<*>> = listOf(

        )
    }
}