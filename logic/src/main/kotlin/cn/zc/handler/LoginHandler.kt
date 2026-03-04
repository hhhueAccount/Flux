package cn.zc.handler

import cn.zc.GameProfile
import cn.zc.handler.LoginCacheProvider.cache
import cn.zc.packet.clientbound.login.LoginFinishPacket
import cn.zc.packet.serverbound.handshake.Intent
import cn.zc.packet.serverbound.handshake.IntentionPacket
import cn.zc.packet.serverbound.login.LoginKnownPacket
import cn.zc.packet.serverbound.login.LoginStartPacket
import com.google.common.eventbus.Subscribe
import org.apache.logging.log4j.kotlin.logger
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
object LoginHandler {

    @Subscribe
    fun onHandshake(intentionPacket: IntentionPacket) {
        if (intentionPacket.intent != Intent.LOGIN) return
        intentionPacket.from.nextState()
        intentionPacket.from.nextState()
    }

    @Subscribe
    fun onLogin(loginStartPacket: LoginStartPacket) {
        logger.trace("[RequestLogin] ${loginStartPacket.from.channel.remoteAddress()}")
        cache.put(
            loginStartPacket.from,
            GameProfile(
                loginStartPacket.playerName,
                loginStartPacket.playerUuid,
                loginStartPacket.from
            )
        )
        loginStartPacket.from.send(
            LoginFinishPacket(
                loginStartPacket.playerUuid,
                loginStartPacket.playerName,
                emptyList()
            )
        )
    }

    @Subscribe
    fun onLoginFinished(loginKnownPacket: LoginKnownPacket) {
        loginKnownPacket.from.nextState()
        logger.trace("[LoginPassed] ${loginKnownPacket.from.channel.remoteAddress()}")
    }
}