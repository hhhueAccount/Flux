package cn.zc.packet.serverbound.handshake

enum class Intent {
    STATUS,
    LOGIN,
    TRANSFER;

    fun id() = ordinal + 1

    companion object {
        fun id(id: Int) = entries[id - 1]
    }
}