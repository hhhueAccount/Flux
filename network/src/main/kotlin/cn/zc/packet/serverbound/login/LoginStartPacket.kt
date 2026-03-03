package cn.zc.packet.serverbound.login

import cn.zc.extension.readUTF8
import cn.zc.extension.readUuid
import cn.zc.extension.writeUTF8
import cn.zc.extension.writeUuid
import cn.zc.packet.serverbound.ServerBoundPacket
import io.netty.buffer.ByteBuf
import java.util.*

data class LoginStartPacket(
    val playerName: String,
    val playerUuid: UUID
) : ServerBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(
        byteBuf.readUTF8(),
        byteBuf.readUuid()
    )

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeUTF8(playerName)
        byteBuf.writeUuid(playerUuid)
    }
}
