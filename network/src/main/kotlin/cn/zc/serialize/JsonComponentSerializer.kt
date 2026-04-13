package cn.zc.serialize

import cn.zc.codec.ByteBufDecoder
import cn.zc.codec.ByteBufEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import net.kyori.adventure.text.Component

@ExperimentalSerializationApi
object JsonComponentSerializer : PacketSerializer<Component>() {
    override val descriptor =
        PrimitiveSerialDescriptor("TextComponent", PrimitiveKind.STRING)

    override fun serializePacket(encoder: ByteBufEncoder, value: Component) {
        encoder.encodeJsonComponent(value)
    }

    override fun deserializePacket(decoder: ByteBufDecoder) =
        decoder.decodeJsonComponent()
}