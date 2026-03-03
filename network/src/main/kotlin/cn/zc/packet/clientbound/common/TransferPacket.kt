package cn.zc.packet.clientbound.common

import cn.zc.extension.readUTF8
import cn.zc.extension.readVarInt
import cn.zc.extension.writeUTF8
import cn.zc.extension.writeVarInt
import cn.zc.packet.clientbound.ClientBoundPacket
import io.netty.buffer.ByteBuf

data class TransferPacket(val host: String, val port: Int) : ClientBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(
        byteBuf.readUTF8(),
        byteBuf.readVarInt()
    )

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeUTF8(host)
        byteBuf.writeVarInt(port)
    }
}
