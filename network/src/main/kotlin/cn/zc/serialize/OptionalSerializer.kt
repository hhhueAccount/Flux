package cn.zc.serialize

import com.google.common.base.Optional
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

data class OptionalSerializer<T : Any>(val kSerializer: KSerializer<T>) : KSerializer<Optional<T>> {
    override val descriptor = SerialDescriptor("Optional", kSerializer.descriptor)

    override fun serialize(encoder: Encoder, value: Optional<T>) {
        val present = value.isPresent
        encoder.encodeBoolean(present)
        if (present) encoder.encodeSerializableValue(kSerializer, value.get())
    }

    override fun deserialize(decoder: Decoder) =
        if (decoder.decodeBoolean()) {
            val value = decoder.decodeSerializableValue(kSerializer)
            Optional.of(value)
        } else {
            Optional.absent()
        }
}
