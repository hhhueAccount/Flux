package cn.zc

import com.google.common.collect.HashBiMap
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.tag.Tag
import io.papermc.paper.registry.tag.TagKey
import org.apache.commons.collections4.map.ListOrderedMap
import org.bukkit.Keyed
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.jetbrains.annotations.ApiStatus
import java.util.stream.Stream

/**
 * @see Registry
 */
@ApiStatus.Internal
class FluxRegistry<T : Keyed> : Registry<T> {
    /**
     * 内部维护的注册表查询用[com.google.common.collect.BiMap]，
     * 可以进行[NamespacedKey]与[T]相互查阅
     */
    private val registryMap = HashBiMap.create<NamespacedKey, T>()

    /**
     * 内部维护的注册表查询用[ListOrderedMap]，
     * 可以进行注册表整数ID号查询
     */
    private val indexedMap = ListOrderedMap<NamespacedKey, T>()

    /**
     * 表示注册表文件在类资源文件中的路径，这个目录下应当具有该注册表所有条目
     * 所对应的Json文件
     */
    private val path: String

    constructor(registryKey: RegistryKey<T>) {
        path = "/${registryKey.key().namespace()}/${registryKey.key().value()}"
        // TODO initialize
    }

    override fun get(key: NamespacedKey): T? {
        TODO("Not yet implemented")
    }

    override fun getKey(value: T): NamespacedKey? {
        TODO("Not yet implemented")
    }

    override fun hasTag(key: TagKey<T>): Boolean {
        TODO("Not yet implemented")
    }

    @Suppress("UnstableApiUsage")
    override fun getTag(key: TagKey<T>): Tag<T> {
        TODO("Not yet implemented")
    }

    @Suppress("UnstableApiUsage")
    override fun getTags(): Collection<Tag<T>> {
        TODO("Not yet implemented")
    }

    override fun stream(): Stream<T> {
        TODO("Not yet implemented")
    }

    override fun keyStream(): Stream<NamespacedKey> {
        TODO("Not yet implemented")
    }

    override fun size(): Int {
        TODO("Not yet implemented")
    }

    override fun iterator(): MutableIterator<T> {
        TODO("Not yet implemented")
    }

    override fun toString() = registryMap.toString()
}