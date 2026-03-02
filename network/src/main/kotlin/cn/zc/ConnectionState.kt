package cn.zc

/**
 * 代表一个会话的游戏连接状态。
 */
enum class ConnectionState {
    /**
     * 握手连接阶段。
     * 这是默认的连接状态。
     *
     * @see Session.state
     */
    HANDSHAKE,

    /**
     * `状态获取`游戏连接状态。
     */
    STATUS,

    /**
     * `登录`游戏连接状态。
     *
     * [cn.zc.packet.serverbound.handshake.IntentionPacket]可以控制客户端或者服务端由[STATUS]转换到这种状态。
     */
    LOGIN,

    /**
     * `配置`客户端连接状态。
     *
     * [LOGIN]阶段结束后会切换到这个状态。
     */
    CONFIGURATION,

    /**
     * `游戏`客户端连接状态。
     *
     * [CONFIGURATION]阶段结束后会切换到这个状态。
     */
    PLAY;

    /**
     * 快速获取通讯协议中对应的下一个状态，如果不存在下一个状态，那么就返回它自己。
     */
    fun next(): ConnectionState = when (this) {
        HANDSHAKE -> STATUS
        STATUS -> LOGIN
        LOGIN -> CONFIGURATION
        CONFIGURATION -> PLAY
        // 不往前面推了
        // 这是一个安全设计
        PLAY -> PLAY
    }
}