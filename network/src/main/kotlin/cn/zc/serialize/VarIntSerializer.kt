package cn.zc.serialize

import cn.zc.codec.ByteBufDecoder
import cn.zc.codec.ByteBufEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor

@ExperimentalSerializationApi
object VarIntSerializer : PacketSerializer<Int>() {
    override val descriptor =
        PrimitiveSerialDescriptor("VarInt", PrimitiveKind.INT)

    override fun serializePacket(encoder: ByteBufEncoder, value: Int) {
        encoder.encodeVarInt(value)
    }

    override fun deserializePacket(decoder: ByteBufDecoder) = decoder.decodeVarInt()
}