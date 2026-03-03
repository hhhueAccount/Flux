package cn.zc.packet.serverbound.handshake

import cn.zc.extension.readUTF8
import cn.zc.extension.readVarInt
import cn.zc.extension.writeUTF8
import cn.zc.extension.writeVarInt
import cn.zc.packet.serverbound.ServerBoundPacket
import io.netty.buffer.ByteBuf

data class IntentionPacket(
    val protocolVersion: Int,
    val serverAddress: String,
    val serverPort: Int,
    val intent: Intent
) : ServerBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(
        byteBuf.readVarInt(),
        byteBuf.readUTF8(),
        byteBuf.readShort().toInt(),
        Intent.id(byteBuf.readVarInt())
    )

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeVarInt(protocolVersion)
        byteBuf.writeUTF8(serverAddress)
        byteBuf.writeShort(serverPort)
        byteBuf.writeVarInt(intent.id())
    }
}
