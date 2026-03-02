package cn.zc

import cn.zc.packet.Packet
import com.google.common.eventbus.EventBus

/**
 * 数据包管理器，提供基于事件总线的数据包分发机制。
 *
 * 这个对象实现了单例模式，作为整个应用中数据包通信的中心枢纽。
 * 它使用[EventBus]实现发布-订阅模式，
 * 允许不同组件之间进行解耦的通信。
 *
 * @see cn.zc.packet.Packet 数据包基类
 * @see EventBus Google Guava事件总线
 */
object Packets {
    /**
     * 事件总线实例，用于管理事件的发布和订阅。
     */
    private val bus: EventBus = EventBus()

    /**
     * 发布一个数据包事件到事件总线。
     *
     * 当调用此方法时，事件总线会将该事件分发给所有注册的监听器中
     * 订阅了该事件类型的方法。
     *
     * 注意：这是一个同步调用，会阻塞直到所有监听器处理完成。
     *
     * @param event 要发布的数据包事件，必须是[cn.zc.packet.Packet]的子类
     */
    fun post(event: Packet) {
        bus.post(event)
    }

    /**
     * 注册一个事件监听器到事件总线。
     *
     * 监听器类中的方法需要使用`@Subscribe`注解标记，并且只能有一个参数，
     * 该参数的类型决定了监听器会接收哪种类型的事件。
     *
     * 示例：
     * ```kotlin
     * class MyListener {
     *     @Subscribe
     *     fun onPacketReceived(packet: MyPacket) {
     *         // 处理MyPacket类型的数据包
     *     }
     * }
     * ```
     *
     * @param listener 要注册的监听器对象
     */
    fun register(listener: Any) {
        bus.register(listener)
    }

    /**
     * 从事件总线注销一个事件监听器。
     *
     * 当监听器不再需要接收对应的事件时，应该调用此方法。
     *
     * @param listener 要注销的监听器对象
     */
    fun unregister(listener: Any) {
        bus.unregister(listener)
    }
}