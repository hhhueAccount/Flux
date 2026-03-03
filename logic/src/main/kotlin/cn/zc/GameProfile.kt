package cn.zc

import cn.zc.packet.clientbound.login.Profile
import java.util.*

/**
 * 在连接进行到`PLAY`阶段之前，玩家信息的承载体
 */
data class GameProfile(
    val playerName: String,
    val playerUuid: UUID,
    val session: Session,
    val profiles: List<Profile> = emptyList(),
    val locale: Locale? = null,
    val brand: String? = null
)
