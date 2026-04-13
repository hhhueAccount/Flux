package cn.zc

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import org.apache.logging.log4j.kotlin.logger
import org.jetbrains.annotations.ApiStatus

object EarlyRegistryLoader {
    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setStrictness(Strictness.STRICT)
        .create()

    @ApiStatus.Internal
    fun loadAll() {
        logger.info("启动Minecraft游戏注册表中...")

        // 加载所有目前Minecraft存在的游戏注册表
        (RegistryAccess.registryAccess() as FluxRegistryAccess).registerAll()
        logger.debug(RegistryAccess.registryAccess().getRegistry(RegistryKey.PAINTING_VARIANT))
//        logger.debug(Art.ORB)

        logger.info("游戏注册表中已加载")
    }
}