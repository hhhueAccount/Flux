package cn.zc

import io.papermc.paper.registry.tag.TagKey
import org.bukkit.Keyed
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import java.util.concurrent.CompletableFuture
import java.util.stream.Stream

class DelayedFluxRegistry<T : Keyed> : Registry<T> {
    private val toComplete: CompletableFuture<Registry<T>> = CompletableFuture()
    private val actualRegistry: Registry<T>?
        get() = if (toComplete.isDone) {
            toComplete.getNow(null)
        } else {
            null
        }

    fun complete(registry: FluxRegistry<T>) = toComplete.complete(registry)

    override fun get(key: NamespacedKey) = actualRegistry?.get(key)

    override fun getKey(value: T) = actualRegistry?.getKey(value)

    override fun hasTag(key: TagKey<T>) = actualRegistry?.hasTag(key) ?: false

    override fun getTag(key: TagKey<T>) =
        actualRegistry?.getTag(key) ?: throw UnsupportedOperationException("Registry not yet available")

    override fun getTags() =
        actualRegistry?.tags ?: emptyList()

    override fun stream(): Stream<T> =
        actualRegistry?.stream() ?: Stream.empty()

    override fun keyStream(): Stream<NamespacedKey> =
        actualRegistry?.keyStream() ?: Stream.empty()

    override fun size() =
        actualRegistry?.size() ?: 0

    override fun iterator(): MutableIterator<T> =
        actualRegistry?.iterator() ?: mutableListOf<T>().iterator()

    override fun toString() = "DelayedFluxRegistry(${actualRegistry ?: "uninitialized"})"
}