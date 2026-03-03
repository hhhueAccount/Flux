package cn.zc.handler

import cn.zc.ListPingBuilder
import cn.zc.Vanilla
import cn.zc.packet.clientbound.status.PongPacket
import cn.zc.packet.clientbound.status.StatusResponsePacket
import cn.zc.packet.serverbound.handshake.Intent
import cn.zc.packet.serverbound.handshake.IntentionPacket
import cn.zc.packet.serverbound.status.PingPacket
import com.google.common.eventbus.Subscribe
import net.kyori.adventure.text.minimessage.MiniMessage
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
object ListPingHandler {

    @Subscribe
    fun onHandshake(intentionPacket: IntentionPacket) {
        if (intentionPacket.intent != Intent.STATUS) return
        intentionPacket.from.nextState()
        intentionPacket.from.send(
            StatusResponsePacket(
                ListPingBuilder.build(
                    Vanilla.VERSION,
                    Vanilla.PROTOCOL_VERSION,
                    description = MiniMessage.miniMessage().deserialize("<blue>HI")
                )
            )
        )
    }

    @Subscribe
    fun onPing(pingPacket: PingPacket) {
        pingPacket.from.send(PongPacket(pingPacket.timestamp))
    }
}