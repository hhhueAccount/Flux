package cn.zc

import java.util.*

/**
 * 一个存储玩家基本信息的简易容器，与[ListPingBuilder]一起工作，
 * 起到辅助作用。
 */
data class SimplePlayerData(val name: String, val uuid: UUID)
