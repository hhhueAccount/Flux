package cn.zc.extension

import cn.zc.Session
import io.netty.channel.Channel

/**
 * [Channel]的扩展函数，提供便捷的[Session]访问
 *
 * 这个扩展允许通过简单的方式从[Channel]对象获取关联的会话：
 * ```kotlin
 * val session = channel.minecraft()
 * ```
 *
 * @return 与该通道关联的[Session]实例
 * @see Session.Companion.get 底层调用的工厂方法
 */
fun Channel.minecraft(): Session {
    return Session.get(this)
}