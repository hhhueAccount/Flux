package cn.zc.registry

import cn.zc.codec.NettyBuf
import cn.zc.packet.Packet
import io.netty.buffer.ByteBuf
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import org.apache.commons.collections4.map.ListOrderedMap
import org.apache.logging.log4j.kotlin.logger
import org.jetbrains.annotations.Contract
import kotlin.reflect.KClass

/**
 * 游戏网络数据包注册表的超类，定义数据包注册表的基本行为。比如：
 * - 用[KSerializer]注册数据包，在幕后记录其ID、其所对应的[KClass]，并且在内部创建数据包初始化器`(ByteBuf) -> Packet`随后记录
 * - 用[Packet]所对应的[KClass]来获取数据包对应的注册ID
 * - 用给定的ID，寻找并且返回数据包初始化器`(ByteBuf) -> Packet`
 */
@ExperimentalSerializationApi
abstract class PacketRegistry {

    /**
     * 数据包注册表的存储位置，它作为[Map]可以用[ListOrderedMap.indexOf]获取元素索引值，
     * 这个索引值作为数据包的ID。
     */
    val readers: MutableMap<KClass<*>, (ByteBuf) -> Packet> = mutableMapOf()

    /**
     * TODO
     */
    val writers: MutableMap<KClass<*>, (ByteBuf, Packet) -> Unit> = mutableMapOf()

    /**
     * 数据包ID存储器
     */
    val idStorge: ArrayList<KClass<*>> = ArrayList()

    /**
     * 快速注册数据包。
     *
     * 仅仅允许子类调用该方法
     */
    @Suppress("UNCHECKED_CAST")
    protected inline fun <reified T : Packet> register(serializer: KSerializer<T>) {
        readers[T::class] = packReader<T>(serializer)
        writers[T::class] = packWriter(serializer) as (ByteBuf, Packet) -> Unit
        idStorge.add(T::class)
    }

    companion object {
        /**
         * 把[KSerializer]快速包装为`(ByteBuf) -> Packet`
         */
        @Contract(pure = true)
        inline fun <reified T : Packet> packReader(serializer: KSerializer<T>): (ByteBuf) -> Packet = {
            NettyBuf.toPacket<T>(serializer, it)
        }

        @Contract(pure = true)
        inline fun <reified T : Packet> packWriter(serializer: KSerializer<T>): (ByteBuf, T) -> Unit =
            { buf: ByteBuf, pac: T ->
                NettyBuf.fromPacket(serializer, buf, pac)
            }
    }

    fun getReader(id: Int): ((ByteBuf) -> Packet)? {
        try {
            return readers.getValue(idStorge[id])
        } catch (_: IndexOutOfBoundsException) {
            logger.error("注册表 $this 中未注册ID为 $id 的数据包")
        }
        return null
    }

    fun getWriter(id: Int): ((ByteBuf, Packet) -> Unit)? {
        try {
            return writers.getValue(idStorge[id])
        } catch (_: IndexOutOfBoundsException) {
            logger.error("注册表 $this 中未注册ID为 $id 的数据包")
        }
        return null
    }

    /**
     * 从[KClass]获取数据包ID。
     *
     * 通常在发送数据包的时候调用，因为发送时需要得知数据包的ID并且写入，而此时能提供的只有它对应的[KClass]，
     * 所以参数被设计为[kClass]
     */
    @Contract(pure = true)
    fun getId(kClass: KClass<*>): Int {
        val result = idStorge.indexOf(kClass)
        if (result == -1) {
            logger.error("注册表 $this 中未注册类型为 ${kClass.simpleName} 的数据包")
        }
        return result
    }
}