package cn.zc.serialize

import cn.zc.codec.ByteBufDecoder
import cn.zc.codec.ByteBufEncoder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor

@OptIn(ExperimentalSerializationApi::class)
object JsonSerializer : ByteBufSerializer<JsonObject>() {
    override val descriptor =
        PrimitiveSerialDescriptor("Json", PrimitiveKind.STRING)

    override fun serializeBuf(encoder: ByteBufEncoder, value: JsonObject) {
        encoder.encodeString(value.toString())
    }

    override fun deserializeBuf(decoder: ByteBufDecoder): JsonObject =
        JsonParser.parseString(decoder.decodeString()).asJsonObject
}