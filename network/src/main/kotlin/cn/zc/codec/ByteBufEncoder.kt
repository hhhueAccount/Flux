package cn.zc.codec

import cn.zc.extension.writeUTF8
import cn.zc.extension.writeUuid
import cn.zc.extension.writeVarInt
import io.netty.buffer.ByteBuf
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import java.util.UUID

@ExperimentalSerializationApi
class ByteBufEncoder(val byteBuf: ByteBuf) : AbstractEncoder() {
    override val serializersModule = EmptySerializersModule()

    override fun encodeValue(value: Any) = throw UnsupportedOperationException("不支持的编码操作")

    override fun encodeByte(value: Byte) {
        byteBuf.writeByte(value.toInt())
    }

    override fun encodeBoolean(value: Boolean) {
        byteBuf.writeBoolean(value)
    }

    override fun encodeDouble(value: Double) {
        byteBuf.writeDouble(value)
    }

    override fun encodeFloat(value: Float) {
        byteBuf.writeFloat(value)
    }

    override fun encodeInt(value: Int) {
        byteBuf.writeInt(value)
    }

    override fun encodeLong(value: Long) {
        byteBuf.writeLong(value)
    }

    override fun encodeShort(value: Short) {
        byteBuf.writeShort(value.toInt())
    }

    override fun encodeString(value: String) {
        byteBuf.writeUTF8(value)
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        if (index !in 0..<enumDescriptor.elementsCount) {
            throw IllegalArgumentException(
                "枚举索引 $index 超出范围 [0, ${enumDescriptor.elementsCount - 1}] " +
                        "枚举类型: ${enumDescriptor.serialName}"
            )
        }

        encodeVarInt(index)
    }

    // ###### Custom Encode ######

    fun encodeVarInt(value: Int) {
        byteBuf.writeVarInt(value)
    }

    fun encodeJsonComponent(component: Component) {
        encodeString(GsonComponentSerializer.gson().serialize(component))
    }

    fun encodeUuid(uuid: UUID) {
        byteBuf.writeUuid(uuid)
    }
}