package cn.zc.serialize

import cn.zc.codec.ByteBufDecoder
import cn.zc.codec.ByteBufEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor

@ExperimentalSerializationApi
object RawBytesSerializer : ByteBufSerializer<ByteArray>() {
    override val descriptor =
        PrimitiveSerialDescriptor("RawBytes", PrimitiveKind.BYTE)

    override fun serializeBuf(encoder: ByteBufEncoder, value: ByteArray) {
        encoder.encodeRawBytes(value)
    }

    override fun deserializeBuf(decoder: ByteBufDecoder) = decoder.decodeRawBytes()
}