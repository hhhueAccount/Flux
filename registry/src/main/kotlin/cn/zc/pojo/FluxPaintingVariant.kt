package cn.zc.pojo

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.Art
import org.bukkit.NamespacedKey

data class FluxPaintingVariant(
    val width: Int,
    val height: Int,
    val title: Component,
    val author: Component,
    val name: Component,
) : Art {
    private lateinit var key: NamespacedKey

    override fun getBlockWidth(): Int {
        TODO("Not yet implemented")
    }

    override fun getBlockHeight(): Int {
        TODO("Not yet implemented")
    }

    override fun getId(): Int {
        TODO("Not yet implemented")
    }

    override fun getKey(): NamespacedKey {
        TODO("Not yet implemented")
    }

    override fun title(): Component? {
        TODO("Not yet implemented")
    }

    override fun author(): Component? {
        TODO("Not yet implemented")
    }

    override fun assetId(): Key {
        TODO("Not yet implemented")
    }

    override fun compareTo(other: Art): Int {
        TODO("Not yet implemented")
    }

    override fun name(): String {
        TODO("Not yet implemented")
    }

    override fun ordinal(): Int {
        TODO("Not yet implemented")
    }
}