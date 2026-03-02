package cn.zc.registry

import cn.zc.ConnectionState
import cn.zc.ConnectionState.*

/**
 * 代表所有连接阶段的数据包注册表。
 * 这里所有的数据包都是向服务端发送的。
 *
 * 你可以根据连接状态匹配到对应的注册表。
 */
object ServerBound {
    fun state(state: ConnectionState) = when(state) {
        HANDSHAKE -> ServerBoundRegistry.Handshake
        STATUS -> ServerBoundRegistry.Status
        LOGIN -> ServerBoundRegistry.Login
        CONFIGURATION -> ServerBoundRegistry.Configuration
        PLAY -> ServerBoundRegistry.Play
    }
}