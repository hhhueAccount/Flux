package cn.zc.packet

import io.netty.buffer.ByteBuf

/**
 * 这是一个空的数据包。
 *
 * 可以用于调试、测试、错误判定。
 * 当执行状态错误而又不得不返回一个数据包对象的时候，可以考虑创建这个对象并且返回。
 */
object EmptyPacket : Packet() {
    override fun serialize(byteBuf: ByteBuf) {
        // 空的，啥都没有
    }
}