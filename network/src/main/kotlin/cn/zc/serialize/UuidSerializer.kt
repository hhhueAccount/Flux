package cn.zc.serialize

import cn.zc.codec.ByteBufDecoder
import cn.zc.codec.ByteBufEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import java.util.*

@ExperimentalSerializationApi
object UuidSerializer : PacketSerializer<UUID>() {

    override val descriptor = buildClassSerialDescriptor("UUID") {
        element("mostSignificantBits", Long.serializer().descriptor)
        element("leastSignificantBits", Long.serializer().descriptor)
    }

    override fun serializePacket(encoder: ByteBufEncoder, value: UUID) {
        encoder.encodeUuid(value)
    }

    override fun deserializePacket(decoder: ByteBufDecoder) = decoder.decodeUuid()
}