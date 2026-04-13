package cn.zc.serialize

import cn.zc.codec.ByteBufDecoder
import cn.zc.codec.ByteBufEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@ExperimentalSerializationApi
abstract class PacketSerializer<T> : KSerializer<T> {

    final override fun serialize(encoder: Encoder, value: T) {
        require(encoder is ByteBufEncoder)
        serializePacket(encoder, value)
    }

    final override fun deserialize(decoder: Decoder): T {
        require(decoder is ByteBufDecoder)
        return deserializePacket(decoder)
    }

    abstract fun serializePacket(encoder: ByteBufEncoder, value: T)
    abstract fun deserializePacket(decoder: ByteBufDecoder): T
}