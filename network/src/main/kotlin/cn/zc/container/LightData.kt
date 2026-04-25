package cn.zc.container

import cn.zc.serialize.NetworkBitSet
import cn.zc.serialize.PrefixedBytes
import kotlinx.serialization.Serializable
import java.util.BitSet

@Serializable
data class LightData(
    val skyLightMask: NetworkBitSet,
    val blockLightMask: NetworkBitSet,
    val emptySkyLightMask: NetworkBitSet,
    val emptyBlockLightMask: NetworkBitSet,
    val skyLight: PrefixedBytes,
    val blockLight: PrefixedBytes
)
