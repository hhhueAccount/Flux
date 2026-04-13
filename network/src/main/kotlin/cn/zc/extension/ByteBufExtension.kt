@file:Suppress("Unused")

package cn.zc.extension

import cn.zc.resource.Identifier
import com.flowpowered.network.util.ByteBufUtils
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.TagStringIO
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.util.Vector
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import kotlin.reflect.KClass

/**
 * 从[ByteBuf]读取一个可变长度整数
 *
 * 有关其中的具体内容，请参阅：
 * [wiki.vg merge](https://minecraft.wiki/w/Java_Edition_protocol/Packets#VarInt_and_VarLong)
 *
 * @return 解码后的整数值
 */
fun ByteBuf.readVarInt(): Int {
    return ByteBufUtils.readVarInt(this)
}

/**
 * 将可变长度整数写入[ByteBuf]
 *
 * 有关其中的具体内容，请参阅：
 * [wiki.vg merge](https://minecraft.wiki/w/Java_Edition_protocol/Packets#VarInt_and_VarLong)
 *
 * @param int 要写入的整数值
 */
fun ByteBuf.writeVarInt(int: Int) {
    ByteBufUtils.writeVarInt(this, int)
}

/**
 * 从[ByteBuf]读取一个以一种特殊方式编码的字符串
 *
 * 这种字符串以一个`VarInt`开头，标志着后方字符串所对应的字节数
 *
 * @return 解码后的字符串
 */
fun ByteBuf.readUTF8(): String {
    return ByteBufUtils.readUTF8(this)
}

/**
 * 将一个以一种特殊方式编码的字符串写入[ByteBuf]
 *
 * 这种字符串以一个`VarInt`开头，标志着后方字符串所对应的字节数
 *
 * @param string 要写入的字符串
 */
fun ByteBuf.writeUTF8(string: String) {
    ByteBufUtils.writeUTF8(this, string)
}

/**
 * 从[ByteBuf]读取一个可变长度长整数
 *
 * 具体内容，请参阅：
 * [wiki.vg merge](https://minecraft.wiki/w/Java_Edition_protocol/Packets#VarInt_and_VarLong)
 *
 * @return 解码后的长整数值
 */
fun ByteBuf.readVarLong(): Long {
    return ByteBufUtils.readVarLong(this)
}

/**
 * 将可变长度长整数写入[ByteBuf]
 *
 * 具体内容请参阅：
 * [wiki.vg merge](https://minecraft.wiki/w/Java_Edition_protocol/Packets#VarInt_and_VarLong)
 *
 * @param long 要写入的长整数值
 */
fun ByteBuf.writeVarLong(long: Long) {
    return ByteBufUtils.writeVarLong(this, long)
}

/**
 * 从[ByteBuf]读取一个[UUID]
 *
 * [UUID]以两个连续的`64`位长整数形式存储：
 * - 第一个`long`:[UUID]的最高有效位
 * - 第二个`long`:[UUID]的最低有效位
 *
 * @return 解码后的 UUID]对象
 */
fun ByteBuf.readUuid(): UUID {
    return UUID(readLong(), readLong())
}

/**
 * 将[UUID]写入[ByteBuf]
 *
 * [UUID]以两个连续的 64 位长整数形式写入。
 * 先写入最高有效位，后写入最低有效位
 *
 * @param uuid 要写入的 UUID 对象
 */
fun ByteBuf.writeUuid(uuid: UUID) {
    writeLong(uuid.mostSignificantBits)
    writeLong(uuid.leastSignificantBits)
}

/**
 * 从[ByteBuf]读取一个位置坐标
 *
 * 将位置坐标编码为一个 64 位长整数，位分配如下：
 * - x: 26 位 (有符号，最高位为符号位)
 * - z: 26 位 (有符号，最高位为符号位)
 * - y: 12 位 (无符号，范围 0-4095)
 *
 * 编码格式：x(26位) + z(26位) + y(12位)
 *
 * @return 包含解码后坐标的[Vector]
 */
fun ByteBuf.readPosition(): Vector {
    val long = readLong()
    val x: Long = long shr 38 // 右移 38 位提取 x 坐标（26 位有符号）
    val y: Long = long shr 26 and 0xfffL // 右移 26 位并掩码提取 y 坐标（12 位无符号）
    val z: Long = long shl 38 shr 38 // 左移清除 x 位，再右移提取 z 坐标（26 位有符号）
    return Vector(x.toInt(), y.toInt(), z.toInt())
}

/**
 * 将[Vector]对象的位置坐标写入[ByteBuf]
 *
 * @param vector 包含坐标的 Vector 对象，坐标值将被转换为整数
 */
fun ByteBuf.writePosition(vector: Vector) {
    writePosition(vector.x.toLong(), vector.y.toLong(), vector.z.toLong())
}

/**
 * 将坐标写入[ByteBuf]
 *
 * 将三个坐标值按照 Minecraft 协议格式打包为 64 位长整数：
 * - x和z坐标取低26位
 * - y坐标取低12位
 *
 * 编码公式:`(x & 0x3ffffff) << 38 | (y & 0xfff) << 26 | (z & 0x3ffffff)`
 *
 * @param x X 坐标值（通常为世界坐标）
 * @param y Y 坐标值（通常为垂直高度）
 * @param z Z 坐标值（通常为世界坐标）
 */
fun ByteBuf.writePosition(x: Long, y: Long, z: Long) {
    writeLong((x and 0x3ffffffL) shl 38 or ((y and 0xfffL) shl 26) or (z and 0x3ffffffL))
}

/**
 * 从[ByteBuf]读取`NBT(Named Binary Tag)`数据
 *
 * @return 读取的`NBT`复合标签
 */
fun ByteBuf.readNbt() = BinaryTagIO
    .reader()
    .read(ByteBufInputStream(this) as InputStream)

/**
 * 将`NBT (Named Binary Tag)`数据写入[ByteBuf]
 *
 * @param nbt 要写入的 NBT 复合标签
 * @see CompoundBinaryTag Minecraft NBT 复合标签类型
 */
fun ByteBuf.writeNbt(nbt: CompoundBinaryTag) {
    BinaryTagIO
        .writer()
        .write(
            nbt,
            ByteBufOutputStream(this) as OutputStream
        )
}

/**
 * 从[ByteBuf]读取[Component]
 *
 * `Minecraft`文本组件以`NBT`格式存储，然后转换为`JSON`字符串，
 * 最后通过`Gson`反序列化为[Component]对象
 *
 * 处理流程：`ByteBuf(NBT) → JSON字符串 → Component对象`
 *
 * @return 反序列化的文本组件，可用于在`Minecraft`中显示富文本
 */
fun ByteBuf.readTextComponent(): Component {
    val nbt = readNbt()

    val json = TagStringIO
        .tagStringIO()
        .asString(nbt)

    return GsonComponentSerializer
        .gson()
        .deserialize(json)
}

/**
 * 将文本组件[Component]写入[ByteBuf]
 *
 * 文本组件先序列化为`JSON`字符串，再转换为`NBT`格式，
 * 最后以二进制形式写入[ByteBuf]
 *
 * 处理流程：`Component对象 → JSON字符串 → ByteBuf(NBT)`
 *
 * @param component 要写入的文本组件
 */
fun ByteBuf.writeTextComponent(component: Component) {
    val json = GsonComponentSerializer
        .gson()
        .serialize(component)

    val nbt = TagStringIO
        .tagStringIO()
        .asTag(json)

    writeNbt(nbt as CompoundBinaryTag)
}

/**
 * 从[ByteBuf]读取位集合[BitSet]
 *
 * 先读取一个`LongArray`，然后把这个数组传入[BitSet.valueOf]中，
 * 得到[BitSet]
 *
 * @return 从字节数据重构的位集合
 */
fun ByteBuf.readBitSet(): BitSet {
    val length = readVarInt()

    val array = LongArray(length)
    for (i in 0 until length) {
        array[i] = readLong()
    }

    return BitSet.valueOf(array)
}

/**
 * 将位集合 (BitSet) 写入 ByteBuf
 *
 * 先进行[BitSet.toLongArray]操作，然后写入该`LogArray`的长度
 * 然后写入每个元素的值
 *
 * @param bitSet 要写入的位集合
 */
fun ByteBuf.writeBitSet(bitSet: BitSet) {
    val longArray = bitSet.toLongArray()
    val length = longArray.size
    writeVarInt(length)

    for (long in longArray) {
        writeLong(long)
    }
}

/**
 * 从[ByteBuf]读取枚举值
 *
 * 枚举值以其序数（ordinal）形式存储为`VarInt`，
 * 通过反射获取枚举类对应的常量数组来恢复枚举值
 *
 * @param enum 枚举类的 KClass 引用
 * @return 从序数解码的枚举值
 */
fun <R : Enum<R>> ByteBuf.readEnum(enum: KClass<R>): R {
    val ordinal = readVarInt()
    return enum.java.enumConstants[ordinal]
}

/**
 * 将枚举值写入[ByteBuf]
 *
 * 枚举值以其序数（ordinal）形式写入为`VarInt`，
 * 解码时通过序数在枚举常量数组中查找对应值
 *
 * @param enum 要写入的枚举值
 */
fun ByteBuf.writeEnum(enum: Enum<*>) {
    writeVarInt(enum.ordinal)
}

/**
 * 从[ByteBuf]读取压缩编码的向量
 *
 * 使用[CompressedVectorHelper]工具类解码压缩的向量数据，
 * 常用于存储实体位置、速度等需要压缩存储的向量数据
 *
 * @return 解码后的 Vector 对象
 */
fun ByteBuf.readLpVector() = CompressedVectorHelper.read(this)

/**
 * 将向量压缩编码并写入[ByteBuf]
 *
 * 使用[CompressedVectorHelper]工具类将向量压缩后写入，
 * 压缩算法优化了网络传输时的数据大小
 *
 * @param vector 要写入的 Vector 对象
 */
fun ByteBuf.writeLpVector(vector: Vector) {
    CompressedVectorHelper.write(this, vector)
}

fun <E> ByteBuf.readList(reader: ByteBuf.() -> E): List<E> {
    val list = mutableListOf<E>()
    for (i in 0 until readVarInt()) {
        list.add(reader())
    }

    return list
}

/**
 * 把一列表的元素写入到[ByteBuf]
 *
 * 注意，其前方写入了列表长度
 */
fun <E> ByteBuf.writeList(values: List<E>, writer: ByteBuf.(E) -> Unit) {
    writeVarInt(values.size)
    for (e in values) {
        writer(e)
    }
}

fun <E> ByteBuf.readKnownLength(expectedLength: Int, reader: ByteBuf.() -> E): List<E> {
    val list = mutableListOf<E>()
    for (i in 0 until expectedLength) {
        list.add(reader())
    }

    return list
}

/**
 * 把一列表的元素写入到[ByteBuf]
 *
 * 注意，其前方没有写入列表长度
 */
fun <E> ByteBuf.writeKnownLength(values: List<E>, writer: ByteBuf.(E) -> Unit) {
    for (e in values) {
        writer(e)
    }
}

fun <T> ByteBuf.readOptional(reader: ByteBuf.() -> T): T? {
    if (readBoolean()) {
        return reader()
    } else {
        return null
    }
}

fun <T> ByteBuf.writeOptional(value: T?, writer: ByteBuf.(T) -> Unit) {
    if (value == null) {
        writeBoolean(false)
    } else {
        writeBoolean(true)
        writer(value)
    }
}

fun ByteBuf.readByteArray(): ByteArray {
    val size = readVarInt()
    val byteArray = ByteArray(size)
    readBytes(byteArray)
    return byteArray
}

fun ByteBuf.writeByteArray(byteArray: ByteArray) {
    writeVarInt(byteArray.size)
    writeBytes(byteArray)
}

fun ByteBuf.readIdentifier() = Identifier.of(readUTF8())

fun ByteBuf.writeIdentifier(identifier: Identifier) {
    writeUTF8(identifier.toString())
}