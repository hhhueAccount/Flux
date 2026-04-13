package cn.zc

import com.google.common.reflect.ClassPath
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import org.apache.logging.log4j.kotlin.logger
import org.bukkit.Keyed
import org.bukkit.Registry
import org.jetbrains.annotations.ApiStatus
import java.io.InputStreamReader
import java.util.MissingResourceException
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors
import kotlin.reflect.KClass
import kotlin.text.startsWith

@Suppress("NonExtendableApiUsage")
class FluxRegistryAccess : RegistryAccess {
    private val registryMap: ConcurrentHashMap<RegistryKey<*>, Registry<*>> = ConcurrentHashMap()

    // TODO to be removed
    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setStrictness(Strictness.STRICT)
        .create()

    @Deprecated("Deprecated by PaperMC since 1.20.6")
    override fun <T : Keyed> getRegistry(type: Class<T>) = getRegistry(byType(type))

    /**
     * Flux尽全力降低了[RegistryKey]在注册表系统中的参与程度，
     * 然而，这几乎是不可能的。
     *
     * Flux也只能维持这种类似工厂模式的设计。
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : Keyed> getRegistry(registryKey: RegistryKey<T>): Registry<T> {
        registryMap[registryKey]?.let {
            return it as Registry<T>
        }

        return registryMap.computeIfAbsent(registryKey) {
            DelayedFluxRegistry<T>()
        } as Registry<T>
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Keyed> byType(type: Class<T>): RegistryKey<T> {
        return LegacyRegistryIdentifiers.classToKeyMap[type] as RegistryKey<T>
    }

    @Suppress("UNCHECKED_CAST")
    @ApiStatus.Internal
    fun <T : Keyed> register(registryKey: RegistryKey<T>, registry: FluxRegistry<T>) {
        // 如果发生意外，没有这个值，就直接填入进去
        registryMap.computeIfAbsent(registryKey) {
            registry
        }

        // 是延迟注册表，那么就把实际值填入
        if (registryMap[registryKey] is DelayedFluxRegistry) {
            (registryMap[registryKey] as DelayedFluxRegistry<T>).complete(registry)
        } // 其他情况不做出任何响应
    }

    /**
     * 注册全部注册表内容。
     *
     * 在加载[Registry]及其子类前，[registerAll]必须被调用，
     * 否则`PaperAPI`无法访问注册表所对应的注册表资源。
     */
    @Suppress("UNCHECKED_CAST")
    @ApiStatus.Internal
    fun registerAll() {
        for (registryKey in resourceKeys()) {
            register(registryKey as RegistryKey<Keyed>, FluxRegistry(registryKey))
            logger.info("注册了 ${registryKey.key()}")
        }
    }

    /**
     * 获取当前存在的所有[RegistryKey]
     */
    fun resourceKeys() = listOf<RegistryKey<*>>(
        RegistryKey.PAINTING_VARIANT
    )

    private fun listResources(prefix: String): Set<String> {
        val classPath = ClassPath.from(javaClass.getClassLoader())
        return classPath.resources.stream()
            .map(ClassPath.ResourceInfo::getResourceName)
            .filter { it.startsWith(prefix) }
            .collect(Collectors.toSet())
    }

    private fun <R : Any> loadResource(path: String, kClass: KClass<R>): R {
        val stream = this::class.java.getResourceAsStream(path) ?: throw MissingResourceException(
            "未找到资源文件：$path",
            this.javaClass.simpleName,
            path
        )
        return gson.fromJson(InputStreamReader(stream), kClass.java)
    }
}