package cn.zc.codec

import cn.zc.container.LightData
import cn.zc.paper.NamespacedKeys
import com.flowpowered.network.util.ByteBufUtils
import com.google.common.base.Optional
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.TagStringIO
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.io.InputStream
import java.util.*

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
    override fun decodeValue() = throw UnsupportedOperationException("不支持的解码操作")
    override fun decodeBoolean() = byteBuf.readBoolean()
    override fun decodeByte() = byteBuf.readByte()
    override fun decodeDouble() = byteBuf.readDouble()
    override fun decodeFloat() = byteBuf.readFloat()
    override fun decodeInt() = byteBuf.readInt()
    override fun decodeLong() = byteBuf.readLong()
    override fun decodeShort() = byteBuf.readShort()
    override fun decodeString(): String = ByteBufUtils.readUTF8(byteBuf)
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

    /**
     * 受到网络堆栈I/O设计方式影响，始终支持顺序解码。
     */
    override fun decodeSequentially() = true
    fun decodeRawBytes(): ByteArray {
        val length = byteBuf.readableBytes()
        if (length == 0) return ByteArray(0)

        val destination = ByteArray(length)
        byteBuf.readBytes(destination)
        return destination
    }

    fun decodePrefixedBytes(): ByteArray {
        val length = decodeVarInt()
        val destination = ByteArray(length)
        byteBuf.readBytes(destination)
        return destination
    }

    fun decodeVarInt() = ByteBufUtils.readVarInt(byteBuf)
    fun decodeVarLong() = ByteBufUtils.readVarLong(byteBuf)
    fun decodeLongArray(): LongArray {
        val length = decodeVarInt()
        val arr = LongArray(length)
        for (i in 0..<length) {
            arr[i] = decodeLong()
        }
        return arr
    }

    fun <T : Any> decodeOptional(kSerializer: KSerializer<T>) = if (decodeBoolean()) {
        Optional.of(decodeSerializableValue(kSerializer))
    } else {
        Optional.absent()
    }

    fun decodePosition(): Vector {
        val compact = decodeLong()
        val x: Long = compact shr 38
        val y: Long = compact shr 26 and 0xfffL
        val z: Long = compact shl 38 shr 38
        return Vector(x.toInt(), y.toInt(), z.toInt())
    }

    fun decodeBitSet(): BitSet = BitSet.valueOf(decodeLongArray())

    fun decodeNBT() = BinaryTagIO
        .reader()
        .read(ByteBufInputStream(byteBuf) as InputStream)

    fun decodeComponentFromNBT(): Component {
        val nbt = decodeNBT()
        val json = TagStringIO
            .tagStringIO()
            .asString(nbt)

        return GsonComponentSerializer
            .gson()
            .deserialize(json)
    }

    fun decodeComponentFromJson() = GsonComponentSerializer.gson().deserialize(decodeString())
    fun decodeUUID() = UUID(decodeLong(), decodeLong())
    fun decodeNamespacedKey() = NamespacedKeys.of(decodeString())
    fun decodeItem(): ItemStack = TODO()
}