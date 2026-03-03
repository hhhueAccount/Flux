package cn.zc.packet.serverbound.login

import cn.zc.extension.readByteArray
import cn.zc.extension.writeByteArray
import cn.zc.packet.serverbound.ServerBoundPacket
import io.netty.buffer.ByteBuf

data class EncryptionResponsePacket(
    val sharedSecret: ByteArray,
    val verifyToken: ByteArray
) : ServerBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(
        byteBuf.readByteArray(),
        byteBuf.readByteArray()
    )

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeByteArray(sharedSecret)
        byteBuf.writeByteArray(verifyToken)
    }
}
