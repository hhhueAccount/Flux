package cn.zc

import java.util.Locale
import java.util.UUID

data class TemporarySessionInfo(
    var playerName: String? = null,
    var playerUuid: UUID? = null,
    var locale: Locale? = null,
    var viewDistance: Int? = null
)
