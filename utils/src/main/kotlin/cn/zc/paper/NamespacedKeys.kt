package cn.zc.paper

import org.bukkit.NamespacedKey

object NamespacedKeys {
    val pattern: Regex = Regex("^[a-z0-9._-]+:[a-z0-9/._-]+$")

    fun isValid(string: String) = pattern.matches(string)

    fun of(string: String): NamespacedKey {
        if (!isValid(string))
            throw IllegalArgumentException("错误的标识格式：$string")

        val split = string.split(":")
        return NamespacedKey(split[0], split[1])
    }
}