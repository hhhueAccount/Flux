package cn.zc.packet.clientbound.configuration

import cn.zc.extension.Identifier
import cn.zc.extension.writeIdentifier
import cn.zc.extension.writeList
import cn.zc.extension.writeNbt
import cn.zc.packet.clientbound.ClientBoundPacket
import io.netty.buffer.ByteBuf

data class RegistryDataPacket(
    val identifier: Identifier,
    val registryList: List<SimpleRegistryEntry>
) : ClientBoundPacket() {
    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeIdentifier(identifier)
        byteBuf.writeList(registryList) {
            byteBuf.writeIdentifier(it.identifier)
            byteBuf.writeNbt(it.nbt)
        }
    }
}
