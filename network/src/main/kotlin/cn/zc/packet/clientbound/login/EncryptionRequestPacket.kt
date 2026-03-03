package cn.zc.packet.clientbound.login

import cn.zc.extension.readByteArray
import cn.zc.extension.readUTF8
import cn.zc.extension.writeByteArray
import cn.zc.extension.writeUTF8
import cn.zc.packet.clientbound.ClientBoundPacket
import io.netty.buffer.ByteBuf

data class EncryptionRequestPacket(
    val serveID: String,
    val publicKey: ByteArray,
    val verifyToken: ByteArray,
    val shouldAuthenticate: Boolean
) : ClientBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(
        byteBuf.readUTF8(),
        byteBuf.readByteArray(),
        byteBuf.readByteArray(),
        byteBuf.readBoolean()
    )

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeUTF8(serveID)
        byteBuf.writeByteArray(publicKey)
        byteBuf.writeByteArray(verifyToken)
        byteBuf.writeBoolean(shouldAuthenticate)
    }
}
