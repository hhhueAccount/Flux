package cn.zc.packet.clientbound.configuration

import cn.zc.extension.readOptional
import cn.zc.extension.readUuid
import cn.zc.extension.writeOptional
import cn.zc.extension.writeUuid
import cn.zc.packet.clientbound.ClientBoundPacket
import io.netty.buffer.ByteBuf
import java.util.*

data class RemoveResourcePackPacket(val packUuid: UUID?) : ClientBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(byteBuf.readOptional(ByteBuf::readUuid))

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeOptional(packUuid, ByteBuf::writeUuid)
    }
}
