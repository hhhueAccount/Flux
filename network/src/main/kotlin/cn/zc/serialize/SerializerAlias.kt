@file:OptIn(ExperimentalSerializationApi::class)

package cn.zc.serialize

import com.google.gson.JsonObject
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import java.util.UUID

/**
 * 请参阅：[教程文档](https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serializers.md#specifying-a-serializer-globally-using-a-typealias)
 */

typealias VarInt = @Serializable(with = VarIntSerializer::class) Int
typealias Json = @Serializable(with = JsonSerializer::class) JsonObject
typealias JsonComponent = @Serializable(with = JsonComponentSerializer::class) Component
typealias McUuid = @Serializable(with = UuidSerializer::class) UUID
typealias PrefixedBytes = @Serializable(with = ByteArraySerializer::class) ByteArray