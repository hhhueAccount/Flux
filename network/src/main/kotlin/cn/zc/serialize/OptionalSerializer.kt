package cn.zc.serialize

import cn.zc.codec.ByteBufDecoder
import cn.zc.codec.ByteBufEncoder
import com.google.common.base.Optional
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor

@ExperimentalSerializationApi
data class OptionalSerializer<T : Any>(val kSerializer: KSerializer<T>) : ByteBufSerializer<Optional<T>>() {
    override val descriptor = SerialDescriptor("Optional", kSerializer.descriptor)

    override fun serializeBuf(encoder: ByteBufEncoder, value: Optional<T>) = encoder.encodeOptional(value, kSerializer)
    override fun deserializeBuf(decoder: ByteBufDecoder) = decoder.decodeOptional(kSerializer)
}
