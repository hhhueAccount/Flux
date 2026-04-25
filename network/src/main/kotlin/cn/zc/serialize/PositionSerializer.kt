package cn.zc.serialize

import cn.zc.codec.ByteBufDecoder
import cn.zc.codec.ByteBufEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import org.bukkit.util.Vector

@ExperimentalSerializationApi
object PositionSerializer : ByteBufSerializer<Vector>() {
    override val descriptor = buildClassSerialDescriptor("Vector") {
        element("x", Int.serializer().descriptor)
        element("y", Int.serializer().descriptor)
        element("z", Int.serializer().descriptor)
    }

    override fun serializeBuf(encoder: ByteBufEncoder, value: Vector) = encoder.encodePosition(value)
    override fun deserializeBuf(decoder: ByteBufDecoder) = decoder.decodePosition()
}