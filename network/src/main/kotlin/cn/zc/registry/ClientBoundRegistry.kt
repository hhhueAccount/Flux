package cn.zc.registry

import cn.zc.packet.clientbound.status.PongPacket
import cn.zc.packet.clientbound.status.StatusResponsePacket

class ClientBoundRegistry {
    object Handshake: Registry() {
        override val packets: List<PacketInfo<*>> = emptyList()
    }

    object Status: Registry() {
        override val packets: List<PacketInfo<*>> = listOf(
            PacketInfo(StatusResponsePacket::class, ::StatusResponsePacket),
            PacketInfo(PongPacket::class, ::PongPacket),
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