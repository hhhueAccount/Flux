package cn.zc.resource

data class Identifier(val namespace: String = "minecraft", val value: String) {

    override fun toString() = "$namespace:$value"

    companion object {
        val pattern: Regex = Regex("^[a-z0-9._-]+:[a-z0-9._-]+$")

        fun isValid(string: String) = pattern.matches(string)

        fun of(string: String): Identifier {
            if (!isValid(string))
                throw IllegalArgumentException("错误的标识格式：$string")
            val split = string.split(":")
            return Identifier(split[0], split[1])
        }
    }
}