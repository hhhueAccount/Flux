package cn.zc.registry

import cn.zc.ConnectionState
import cn.zc.ConnectionState.*
import org.jetbrains.annotations.ApiStatus

/**
 * 代表所有连接阶段的数据包注册表。
 * 这里所有的数据包都是向客户端发送的。
 *
 * 你可以根据连接状态匹配到对应的注册表。
 */
object ClientBound {
    @ApiStatus.Internal
    fun state(state: ConnectionState) = when (state) {
        HANDSHAKE -> ClientBoundRegistry.Handshake
        STATUS -> ClientBoundRegistry.Status
        LOGIN -> ClientBoundRegistry.Login
        CONFIGURATION -> ClientBoundRegistry.Configuration
        PLAY -> ClientBoundRegistry.Play
    }
}