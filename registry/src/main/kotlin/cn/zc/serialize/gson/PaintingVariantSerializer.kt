package cn.zc.serialize.gson

import cn.zc.pojo.FluxPaintingVariant
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object PaintingVariantSerializer : JsonDeserializer<FluxPaintingVariant> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): FluxPaintingVariant {
        TODO("Not yet implemented")
    }
}