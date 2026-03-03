package cn.zc.packet.clientbound.configuration

import cn.zc.extension.Identifier
import net.kyori.adventure.nbt.CompoundBinaryTag

data class SimpleRegistryEntry(val identifier: Identifier, val nbt: CompoundBinaryTag)
