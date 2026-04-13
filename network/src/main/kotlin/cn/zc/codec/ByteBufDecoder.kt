package cn.zc.codec

import cn.zc.extension.readUTF8
import cn.zc.extension.readUuid
import cn.zc.extension.readVarInt
import io.netty.buffer.ByteBuf
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.jetbrains.annotations.ApiStatus

/**
 * [ByteBuf]的数据解码器，把[ByteBuf]中的二进制数据读取到[cn.zc.packet.Packet]，
 * 该解码器针对传入的[byteBuf]进行操作，读取其中的数据。
 *
 * 该类只提供基本类型的解码方式，
 * 其他数据类型的解码逻辑需要通过[decodeSerializableValue]委托给外部编写的解码逻辑。
 *
 * 另外，请勿调用[decodeValue]，因为[ByteBuf]从设计逻辑来看不具备通用型数据读取方式！
 */
@ExperimentalSerializationApi
data class ByteBufDecoder(val byteBuf: ByteBuf) : AbstractDecoder() {

    /**
     * @see kotlinx.serialization.encoding.Decoder.serializersModule
     */
    override val serializersModule = EmptySerializersModule()

    /**
     * @see decodeElementIndex
     */
    private var index: Int = 0

    /**
     * 提供一套按照顺序发放的元素序列号，其具体含义请参见官方说明文档与入门教程。
     */
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (index >= descriptor.elementsCount)
            return CompositeDecoder.DECODE_DONE
        return index++
    }

    /**
     * 请勿调用此方法，因为[ByteBuf]从设计逻辑来看不具备通用型数据读取方式！
     */
    @ApiStatus.Internal
    override fun decodeValue() = throw UnsupportedOperationException("不支持的解码操作")

    override fun decodeBoolean() = byteBuf.readBoolean()
    override fun decodeByte() = byteBuf.readByte()
    override fun decodeDouble() = byteBuf.readDouble()
    override fun decodeFloat() = byteBuf.readFloat()
    override fun decodeInt() = byteBuf.readInt()
    override fun decodeLong() = byteBuf.readLong()
    override fun decodeShort() = byteBuf.readShort()
    override fun decodeString() = byteBuf.readUTF8()

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        val ordinal = decodeVarInt()
        if (ordinal !in 0..<enumDescriptor.elementsCount) {
            throw IllegalArgumentException(
                "枚举索引 $ordinal 超出范围 [0, ${enumDescriptor.elementsCount - 1}] " +
                        "枚举类型: ${enumDescriptor.serialName}"
            )
        }

        return ordinal
    }

    // ###### Custom Decode ######
    fun decodeVarInt() = byteBuf.readVarInt()
    fun decodeJsonComponent() = GsonComponentSerializer.gson().deserialize(decodeString())
    fun decodeUuid() = byteBuf.readUuid()

    /**
     * 受到网络堆栈I/O设计方式影响，始终支持顺序解码。
     */
    override fun decodeSequentially() = true
}