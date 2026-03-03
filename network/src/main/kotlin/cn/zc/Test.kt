package cn.zc

import cn.zc.packet.clientbound.status.StatusResponsePacket
import cn.zc.packet.serverbound.handshake.Intent
import cn.zc.packet.serverbound.handshake.IntentionPacket
import cn.zc.packet.serverbound.status.StatusRequestPacket
import com.google.common.eventbus.Subscribe
import com.google.gson.JsonObject
import org.apache.logging.log4j.kotlin.logger
import org.jetbrains.annotations.ApiStatus

/**
 * 测试用例，即将被删除
 */
@Deprecated("即将删除")
@ApiStatus.ScheduledForRemoval
fun main() {
    Packets.register(TestListener)
    val networkClient = NetworkClient(host = "on.imc.cab")
    networkClient.launch()
    networkClient.sendSync(
        IntentionPacket(
            772,
            "localhost",
            25565,
            Intent.STATUS
        )
    )
    networkClient.session.nextState()
    networkClient.session.sendSync(StatusRequestPacket())
}

@Deprecated("即将删除")
@ApiStatus.ScheduledForRemoval
object TestListener {
    @Subscribe
    fun onHandshake(intentionPacket: IntentionPacket) {
        logger.trace("[List Ping] ${intentionPacket.from.channel.remoteAddress()}")
        intentionPacket.from.nextState()
        intentionPacket.from.send(StatusResponsePacket(JsonObject()))
    }

    @Subscribe
    fun onStatus(statusResponsePacket: StatusResponsePacket) {
        logger.trace("got it!")
    }
}