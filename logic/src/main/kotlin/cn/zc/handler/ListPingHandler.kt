@file:Suppress("UNUSED")

package cn.zc.handler

import cn.zc.ListPingBuilder
import cn.zc.Vanilla
import cn.zc.packet.clientbound.common.PongPacket
import cn.zc.packet.clientbound.status.StatusResponsePacket
import cn.zc.packet.serverbound.common.PingPacket
import cn.zc.packet.serverbound.handshake.IntentionPacket
import cn.zc.packet.serverbound.handshake.IntentionPacket.Intention
import cn.zc.packet.serverbound.status.StatusRequestPacket
import com.google.common.eventbus.Subscribe
import net.kyori.adventure.text.minimessage.MiniMessage
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
object ListPingHandler {

    @Subscribe
    fun onHandshake(intentionPacket: IntentionPacket) {
        if (intentionPacket.intention != Intention.STATUS) return
        intentionPacket.from.nextState()
    }

    @Subscribe
    fun onRequestListPing(statusRequestPacket: StatusRequestPacket) {
        statusRequestPacket.from.send(
            StatusResponsePacket(
                ListPingBuilder.build(
                    Vanilla.VERSION,
                    Vanilla.PROTOCOL_VERSION,
                    description = MiniMessage.miniMessage().deserialize("<gradient:#FF22AA:#AA22FF>你好，欢迎使用Flux！")
                )
            )
        )
    }

    @Subscribe
    fun onPing(pingPacket: PingPacket) {
        pingPacket.from.send(PongPacket(pingPacket.timestamp))
    }
}