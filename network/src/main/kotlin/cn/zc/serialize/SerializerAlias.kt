@file:OptIn(ExperimentalSerializationApi::class)

package cn.zc.serialize

import com.google.gson.JsonObject
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey
import java.util.*

/**
 * 请参阅：[教程文档](https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serializers.md#specifying-a-serializer-globally-using-a-typealias)
 */

typealias VarInt = @Serializable(with = VarIntSerializer::class) Int
typealias VarLong = @Serializable(with = VarLongSerializer::class) Long
typealias NetworkBitSet = @Serializable(with = BitSetSerializer::class) BitSet

typealias Json = @Serializable(with = JsonSerializer::class) JsonObject
typealias JsonTextComponent = @Serializable(with = ComponentJsonSerializer::class) Component

typealias RawBytes = @Serializable(with = RawBytesSerializer::class) ByteArray
typealias PrefixedBytes = @Serializable(with = ByteArraySerializer::class) ByteArray

typealias Identifier = @Serializable(with = NamespacedKeySerializer::class) NamespacedKey
typealias NetworkUUID = @Serializable(with = UUIDSerializer::class) UUID