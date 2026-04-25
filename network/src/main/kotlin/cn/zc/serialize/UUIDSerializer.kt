package cn.zc.serialize

import cn.zc.codec.ByteBufDecoder
import cn.zc.codec.ByteBufEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import java.util.*

@ExperimentalSerializationApi
object UUIDSerializer : ByteBufSerializer<UUID>() {
    override val descriptor = buildClassSerialDescriptor("UUID") {
        element("mostSignificantBits", Long.serializer().descriptor)
        element("leastSignificantBits", Long.serializer().descriptor)
    }

    override fun serializeBuf(encoder: ByteBufEncoder, value: UUID) = encoder.encodeUUID(value)
    override fun deserializeBuf(decoder: ByteBufDecoder) = decoder.decodeUUID()
}