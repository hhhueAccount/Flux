package cn.zc.handler

import com.flowpowered.network.util.ByteBufUtils
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageCodec
import org.apache.logging.log4j.kotlin.logger

/**
 * 数据帧处理器
 *
 * 负责处理网络数据的帧定界，确保数据包的完整性和正确解析。
 * 使用VarInt编码数据包长度，实现可变长度数据包的处理。
 *
 * 参考自[Glowstone](https://github.com/GlowstoneMC/Glowstone)和
 * [Minestom](https://github.com/Minestom/Minestom)的项目设计理念
 */
class FramingHandler : ByteToMessageCodec<ByteBuf?>() {
    private fun readableVarInt(buf: ByteBuf): Boolean {
        if (buf.readableBytes() > 5) {
            // maximum varint size
            return true
        }

        val idx = buf.readerIndex()
        var `in`: Byte
        do {
            if (buf.readableBytes() < 1) {
                buf.readerIndex(idx)
                return false
            }
            `in` = buf.readByte()
        } while ((`in`.toInt() and 0x80) != 0)

        buf.readerIndex(idx)
        return true
    }

    override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf?, out: ByteBuf) {
        ByteBufUtils.writeVarInt(out, msg!!.readableBytes())
        out.writeBytes(msg)
    }

    override fun decode(ctx: ChannelHandlerContext, input: ByteBuf, out: MutableList<Any?>) {
        // check for length field readability
        input.markReaderIndex()
        if (!readableVarInt(input)) {
            return
        }

        // check for contents readability
        val length = ByteBufUtils.readVarInt(input)

        if (input.readableBytes() < length) {
            input.resetReaderIndex()
            return
        }

        // read contents into buf
        val buf = ctx.alloc().buffer(length)
        input.readBytes(buf, length)
        out.add(buf)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        logger.error("一个错误在写入或者解析数据包长度时发生了", cause)
    }
}