package cn.zc.packet.clientbound.login

import cn.zc.extension.readVarInt
import cn.zc.extension.writeVarInt
import cn.zc.packet.clientbound.ClientBoundPacket
import io.netty.buffer.ByteBuf

data class SetCompressionPacket(val threshold: Int) : ClientBoundPacket() {
    constructor(byteBuf: ByteBuf): this(byteBuf.readVarInt())

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeVarInt(threshold)
    }
}
