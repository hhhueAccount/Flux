package cn.zc.packet.clientbound.login

import cn.zc.extension.*
import cn.zc.extension.readList
import cn.zc.extension.readOptional
import cn.zc.extension.readUTF8
import cn.zc.packet.clientbound.ClientBoundPacket
import io.netty.buffer.ByteBuf
import java.util.*

data class LoginFinishPacket(
    val playerUuid: UUID,
    val playerName: String,
    val profiles: List<Profile>
) : ClientBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(
        byteBuf.readUuid(),
        byteBuf.readUTF8(),
        byteBuf.readList {
            val name = byteBuf.readUTF8()
            val value = byteBuf.readUTF8()
            val signature = byteBuf.readOptional(ByteBuf::readUTF8)
            Profile(name, value, signature)
        }
    )

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeUuid(playerUuid)
        byteBuf.writeUTF8(playerName)
        byteBuf.writeList(profiles) {
            writeUTF8(it.name)
            writeUTF8(it.value)
            if (it.signature == null) {
                writeBoolean(false)
            } else {
                writeBoolean(true)
                writeUTF8(it.signature)
            }
        }
    }
}
