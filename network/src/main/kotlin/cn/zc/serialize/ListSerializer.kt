package cn.zc.serialize

import cn.zc.codec.ByteBufDecoder
import cn.zc.codec.ByteBufEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor

@ExperimentalSerializationApi
data class ListSerializer<T>(val kSerializer: KSerializer<T>) : ByteBufSerializer<List<T>>() {
    override val descriptor = SerialDescriptor("List", kSerializer.descriptor)

    override fun serializeBuf(encoder: ByteBufEncoder, value: List<T>) {
        encoder.encodeVarInt(value.size)
        for (t in value) {
            encoder.encodeSerializableValue(kSerializer, t)
        }
    }

    override fun deserializeBuf(decoder: ByteBufDecoder): List<T> {
        val size = decoder.decodeVarInt()
        val list = ArrayList<T>(size)
        for (i in 0 until size) {
            list.add(decoder.decodeSerializableValue(kSerializer))
        }
        return list
    }
}