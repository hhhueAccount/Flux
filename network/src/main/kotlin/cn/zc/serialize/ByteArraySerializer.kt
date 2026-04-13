package cn.zc.serialize

import cn.zc.codec.ByteBufDecoder
import cn.zc.codec.ByteBufEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor

@ExperimentalSerializationApi
object ByteArraySerializer : PacketSerializer<ByteArray>() {
    override val descriptor =
        PrimitiveSerialDescriptor("ByteArray", PrimitiveKind.BYTE)

    override fun serializePacket(encoder: ByteBufEncoder, value: ByteArray) {
        encoder.encodeVarInt(value.size)
        for (b in value) {
            encoder.encodeByte(b)
        }
    }

    override fun deserializePacket(decoder: ByteBufDecoder): ByteArray {
        val size = decoder.decodeVarInt()
        val array = ByteArray(size)
        for (i in 0 until size) {
            array[i] = decoder.decodeByte()
        }
        return array
    }
}