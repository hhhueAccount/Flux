@file:Suppress("UNUSED")

package cn.zc.handler

import cn.zc.ConnectionState
import cn.zc.packet.clientbound.login.LoginSuccessPacket
import cn.zc.packet.serverbound.handshake.IntentionPacket
import cn.zc.packet.serverbound.login.LoginAcknowledgedPacket
import cn.zc.packet.serverbound.login.LoginStartPacket
import com.google.common.eventbus.Subscribe
import kotlinx.serialization.ExperimentalSerializationApi
import org.apache.logging.log4j.kotlin.logger
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
object LoginHandler {

    @Subscribe
    fun onHandshake(intentionPacket: IntentionPacket) {
        if (intentionPacket.intention != IntentionPacket.Intention.LOGIN) return
        intentionPacket.from.jumpState(ConnectionState.LOGIN)
    }

    @Subscribe
    @ExperimentalSerializationApi
    fun onLogin(loginStartPacket: LoginStartPacket) {
        val from = loginStartPacket.from

        logger.trace("[RequestLogin] ${from.channel.id()}")
        from.sessionInfo?.playerName = loginStartPacket.playerName
        from.sessionInfo?.playerUuid = loginStartPacket.playerUuid
        from.send(
            LoginSuccessPacket(
                loginStartPacket.playerUuid,
                loginStartPacket.playerName,
                emptyList()
            )
        )
    }

    @Subscribe
    @ExperimentalSerializationApi
    fun onLoginFinished(loginKnownPacket: LoginAcknowledgedPacket) {
        loginKnownPacket.from.nextState()
        logger.trace("[LoginPassed] ${loginKnownPacket.from.channel.id()}")
    }
}