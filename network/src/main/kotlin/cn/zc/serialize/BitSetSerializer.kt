package cn.zc.serialize

import cn.zc.codec.ByteBufDecoder
import cn.zc.codec.ByteBufEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.LongArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import java.util.*

@ExperimentalSerializationApi
object BitSetSerializer : ByteBufSerializer<BitSet>() {
    override val descriptor =
        SerialDescriptor("BitSet", LongArraySerializer().descriptor)

    override fun serializeBuf(encoder: ByteBufEncoder, value: BitSet) = encoder.encodeBitSet(value)
    override fun deserializeBuf(decoder: ByteBufDecoder) = decoder.decodeBitSet()
}