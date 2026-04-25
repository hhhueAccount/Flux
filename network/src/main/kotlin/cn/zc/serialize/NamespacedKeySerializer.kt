package cn.zc.serialize

import cn.zc.codec.ByteBufDecoder
import cn.zc.codec.ByteBufEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import org.bukkit.NamespacedKey

@ExperimentalSerializationApi
object NamespacedKeySerializer : ByteBufSerializer<NamespacedKey>() {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("NamespacedKey", PrimitiveKind.STRING)

    override fun serializeBuf(encoder: ByteBufEncoder, value: NamespacedKey) {
        encoder.encodeNamespacedKey(value)
    }

    override fun deserializeBuf(decoder: ByteBufDecoder) = decoder.decodeNamespacedKey()
}