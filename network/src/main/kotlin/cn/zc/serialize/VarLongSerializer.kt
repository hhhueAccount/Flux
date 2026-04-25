package cn.zc.serialize

import cn.zc.codec.ByteBufDecoder
import cn.zc.codec.ByteBufEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor

@ExperimentalSerializationApi
object VarLongSerializer : ByteBufSerializer<Long>() {
    override val descriptor = PrimitiveSerialDescriptor("VarLong", PrimitiveKind.LONG)

    override fun serializeBuf(encoder: ByteBufEncoder, value: Long) = encoder.encodeVarLong(value)
    override fun deserializeBuf(decoder: ByteBufDecoder) = decoder.decodeVarLong()
}