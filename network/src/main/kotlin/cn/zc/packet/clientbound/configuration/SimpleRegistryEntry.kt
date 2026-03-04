package cn.zc.packet.clientbound.configuration

import cn.zc.resource.Identifier
import net.kyori.adventure.nbt.CompoundBinaryTag

data class SimpleRegistryEntry(val identifier: Identifier, val nbt: CompoundBinaryTag)
