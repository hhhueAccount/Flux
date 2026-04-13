package cn.zc.codec

import cn.zc.packet.Packet
import io.netty.buffer.ByteBuf
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy

/**
 * [ByteBuf]数据解析便捷工具类。
 */
object NettyBuf {

    /**
     * 把数据从[Packet]解析到[ByteBuf]。
     */
    @Suppress("UNCHECKED_CAST")
    @ExperimentalSerializationApi
    inline fun <reified T : Packet> fromPacket(
        serializer: SerializationStrategy<*>,
        byteBuf: ByteBuf,
        packet: T,
    ) {
        val encoder = ByteBufEncoder(byteBuf)
        encoder.encodeSerializableValue(serializer as SerializationStrategy<T>, packet)
    }

    /**
     * 把数据从[ByteBuf]解析到[Packet]
     */
    @Suppress("UNCHECKED_CAST")
    @ExperimentalSerializationApi
    inline fun <reified T : Packet> toPacket(
        deserializer: DeserializationStrategy<*>,
        byteBuf: ByteBuf
    ): Packet {
        val decoder = ByteBufDecoder(byteBuf)
        return decoder.decodeSerializableValue(deserializer as DeserializationStrategy<T>)
    }
}