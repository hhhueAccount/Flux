package cn.zc

import io.leangen.geantyref.GenericTypeReflector
import io.papermc.paper.registry.RegistryKey
import org.jetbrains.annotations.ApiStatus
import java.lang.reflect.ParameterizedType

/**
 * 来自Paper
 */
object LegacyRegistryIdentifiers {
    @get:ApiStatus.Internal
    val classToKeyMap: Map<Class<*>, RegistryKey<*>> by lazy {
        val map = HashMap<Class<*>, RegistryKey<*>>()
        for (field in RegistryKey::class.java.fields) {
            if (field.type == RegistryKey::class.java) {
                val legacyType =
                    GenericTypeReflector.erase((field.genericType as ParameterizedType).actualTypeArguments[0])
                map[legacyType] = field.get(null) as RegistryKey<*>
            }
        }
        return@lazy map
    }
}