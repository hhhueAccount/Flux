package cn.zc.pojo

import org.bukkit.NamespacedKey
import org.bukkit.damage.DamageEffect
import org.bukkit.damage.DamageScaling
import org.bukkit.damage.DamageType
import org.bukkit.damage.DeathMessageType
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
data class FluxDamageType(
    private val messageId: String,
    private val exhaustion: Float,
    private val scaling: DamageScaling,
    private val effects: DamageEffect,
    private val deathMessageType: DeathMessageType
) : DamageType {
    private lateinit var key: NamespacedKey

    override fun getTranslationKey(): String {
        TODO("Not yet implemented")
    }

    override fun getDamageScaling() = scaling

    override fun getDamageEffect() = effects

    override fun getDeathMessageType() = deathMessageType

    override fun getExhaustion() = exhaustion

    override fun getKey() = key
}
