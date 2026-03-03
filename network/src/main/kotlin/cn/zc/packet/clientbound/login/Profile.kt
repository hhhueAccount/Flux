package cn.zc.packet.clientbound.login

data class Profile(
    val name: String,
    val value: String,
    val signature: String?
)
