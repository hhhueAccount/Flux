package cn.zc.codec

import cn.zc.extension.writeUTF8
import cn.zc.extension.writeVarInt
import cn.zc.packet.Packet
import cn.zc.serialize.VarInt
import io.netty.buffer.Unpooled
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@ExperimentalSerializationApi
class NettyBufTest {

    @Test
    fun testDecode() {
        val buf = Unpooled.buffer()
        buf.writeInt(2)
        buf.writeUTF8("TEST")

        val packet = NettyBuf.toPacket<TestPacket>(TestPacket.serializer(), buf)

        assertEquals(TestPacket(2, "TEST"), packet)
        buf.release()
    }

    @Test
    fun testEncode() {
        val buf1 = Unpooled.buffer()
        val buf2 = Unpooled.buffer()

        buf2.writeInt(1)
        buf2.writeUTF8("HELLO")

        val packet = TestPacket(1, "HELLO")
        NettyBuf.fromPacket(TestPacket.serializer(), buf1, packet)

        assertEquals(buf1, buf2)
        buf1.release()
        buf2.release()
    }

    @Test
    fun testAdvancedDecode() {
        val buf = Unpooled.buffer()
        buf.writeVarInt(23)

        val packet = NettyBuf.toPacket<AdvancedPacket>(AdvancedPacket.serializer(), buf)

        assertEquals(AdvancedPacket(23), packet)
        buf.release()
    }

    @Test
    fun testAdvancedEncode() {
        val buf1 = Unpooled.buffer()
        val buf2 = Unpooled.buffer()

        buf2.writeVarInt(15)

        val packet = AdvancedPacket(15)
        NettyBuf.fromPacket(AdvancedPacket.serializer(), buf1, packet)

        assertEquals(buf1, buf2)
        buf1.release()
        buf2.release()
    }

    /**
     * 一个简单的数据包类型，字段类型都是基础数据类型。
     */
    @Serializable
    data class TestPacket(val num: Int, val str: String) : Packet()

    @Serializable
    data class AdvancedPacket(val num: VarInt) : Packet()
}