package cn.zc.codec

import cn.zc.container.LightData
import com.flowpowered.network.util.ByteBufUtils
import com.google.common.base.Optional
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufOutputStream
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.TagStringIO
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.NamespacedKey
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.io.OutputStream
import java.util.*

@ExperimentalSerializationApi
class ByteBufEncoder(val byteBuf: ByteBuf) : AbstractEncoder() {
    /**
     * @see kotlinx.serialization.encoding.Decoder.serializersModule
     */
    override val serializersModule = EmptySerializersModule()

    /**
     * 请勿调用此方法，因为[ByteBuf]从设计逻辑来看不具备通用型数据读取方式！
     */
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

    override fun encodeString(value: String) = ByteBufUtils.writeUTF8(byteBuf, value)
    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        if (index !in 0..<enumDescriptor.elementsCount) {
            throw IllegalArgumentException(
                "枚举索引 $index 超出范围 [0, ${enumDescriptor.elementsCount - 1}] " +
                        "枚举类型: ${enumDescriptor.serialName}"
            )
        }

        encodeVarInt(index)
    }

    fun encodeRawBytes(bytes: ByteArray) {
        byteBuf.writeBytes(bytes)
    }

    fun encodePrefixedBytes(bytes: ByteArray) {
        encodeVarInt(bytes.size)
        encodeRawBytes(bytes)
    }

    fun encodeVarInt(value: Int) = ByteBufUtils.writeVarInt(byteBuf, value)
    fun encodeVarLong(value: Long) = ByteBufUtils.writeVarLong(byteBuf, value)
    fun encodeLongArray(longArray: LongArray) {
        encodeVarInt(longArray.size)
        for (l in longArray) {
            encodeLong(l)
        }
    }

    fun <T : Any> encodeOptional(optional: Optional<T>, kSerializer: KSerializer<T>) {
        encodeBoolean(optional.isPresent)
        if (optional.isPresent) encodeSerializableValue(kSerializer, optional.get())
    }

    fun encodePosition(vector: Vector) = encodeLong(
        (vector.x.toLong() and 0x3ffffffL) shl 38 or
                (vector.y.toLong() and 0xfffL) shl 26 or
                (vector.z.toLong() and 0x3ffffffL)
    )

    fun encodeBitSet(bitSet: BitSet) = encodeLongArray(bitSet.toLongArray())

    fun encodeNBT(nbt: CompoundBinaryTag) = BinaryTagIO.writer()
        .write(
            nbt,
            ByteBufOutputStream(byteBuf) as OutputStream
        )

    fun encodeComponentAsNBT(component: Component) {
        val json = GsonComponentSerializer
            .gson()
            .serialize(component)
        val nbt = TagStringIO
            .tagStringIO()
            .asTag(json)

        encodeNBT(nbt as CompoundBinaryTag)
    }

    fun encodeComponentAsJson(component: Component) =
        encodeString(GsonComponentSerializer.gson().serialize(component))

    fun encodeUUID(uuid: UUID) {
        encodeLong(uuid.mostSignificantBits)
        encodeLong(uuid.leastSignificantBits)
    }

    fun encodeNamespacedKey(key: NamespacedKey) = encodeString(key.asString())
    fun encodeItem(item: ItemStack) {
        TODO()
    }
}