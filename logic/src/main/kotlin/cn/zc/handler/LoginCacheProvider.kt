package cn.zc.handler

import cn.zc.GameProfile
import cn.zc.Session
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import org.jetbrains.annotations.ApiStatus
import java.time.Duration

@ApiStatus.Experimental
object LoginCacheProvider {
    val cache: Cache<Session, GameProfile> = CacheBuilder.newBuilder()
        .expireAfterAccess(Duration.ofMinutes(3))
        .expireAfterWrite(Duration.ofMinutes(3))
        .build()
}