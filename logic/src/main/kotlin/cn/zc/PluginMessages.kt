package cn.zc

import cn.zc.resource.Identifier
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import org.jetbrains.annotations.ApiStatus

/**
 * 插件消息管理器
 *
 * 提供插件间的消息发布/订阅机制，允许不同插件模块通过标识符进行通信。
 * 使用观察者模式实现，支持一对多的消息分发。
 *
 * 注意：所有操作都是线程不安全的，调用者需要自行处理同步问题。
 */
@ApiStatus.Experimental
object PluginMessages {
    /**
     * 消息监听器注册表
     *
     * 使用 Guava 的 Multimap 存储标识符到监听器的映射关系，
     * 允许一个标识符对应多个监听器。
     */
    private val registered: Multimap<Identifier, MessageListener> = ArrayListMultimap.create()

    /**
     * 发布消息到指定标识符的所有监听器
     *
     * @param identifier 消息标识符，用于确定接收消息的监听器
     * @param payload 消息负载，以字节数组形式传递
     *
     * 注意：消息会按注册顺序同步发送给所有监听器，
     * 如果某个监听器抛出异常，不会影响其他监听器的执行。
     */
    fun postMessage(identifier: Identifier, payload: ByteArray) {
        registered.get(identifier).forEach {
            it.onReceive(payload)
        }
    }

    /**
     * 为指定标识符添加消息监听器
     *
     * @param identifier 要监听的标识符
     * @param listener 消息监听器实例
     *
     * 注意：同一个监听器可以注册到多个不同的标识符，
     * 同一个标识符也可以注册多个监听器。
     */
    fun addListener(identifier: Identifier, listener: MessageListener) {
        registered.put(identifier, listener)
    }

    /**
     * 移除指定标识符的特定监听器
     *
     * @param identifier 标识符
     * @param listener 要移除的监听器实例
     *
     * @return 如果成功移除了监听器则返回 true
     */
    fun removeListener(identifier: Identifier, listener: MessageListener) {
        registered.remove(identifier, listener)
    }

    /**
     * 移除指定标识符的所有监听器
     *
     * @param identifier 要清除监听器的标识符
     */
    fun removeListener(identifier: Identifier) {
        registered.removeAll(identifier)
    }
}