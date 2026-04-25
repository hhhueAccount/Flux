package cn.zc.serialize

import cn.zc.codec.ByteBufDecoder
import cn.zc.codec.ByteBufEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import net.kyori.adventure.text.Component

@ExperimentalSerializationApi
object ComponentJsonSerializer : ByteBufSerializer<Component>() {
    override val descriptor =
        PrimitiveSerialDescriptor("TextComponent", PrimitiveKind.STRING)

    override fun serializeBuf(encoder: ByteBufEncoder, value: Component) {
        encoder.encodeComponentAsJson(value)
    }

    override fun deserializeBuf(decoder: ByteBufDecoder) =
        decoder.decodeComponentFromJson()
}