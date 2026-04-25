package cn.zc.handler

import com.flowpowered.network.util.ByteBufUtils
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.DecoderException
import io.netty.handler.codec.EncoderException
import io.netty.handler.codec.MessageToMessageCodec
import org.apache.logging.log4j.kotlin.logger
import java.util.zip.Deflater
import java.util.zip.Inflater

/**
 * Experimental pipeline component.
 *
 * 网络数据压缩处理器，负责对网络传输的数据进行压缩和解压操作。
 * 使用DEFLATE算法进行数据压缩，以减少网络传输的数据量。
 *
 * 参考自[Glowstone](https://github.com/GlowstoneMC/Glowstone)和
 * [Minestom](https://github.com/Minestom/Minestom)的项目设计理念
 */
class CompressionHandler(private val threshold: Int) : MessageToMessageCodec<ByteBuf?, ByteBuf?>() {
    private val inflater: Inflater = Inflater()
    private val deflater: Deflater = Deflater(COMPRESSION_LEVEL)

    /**
     * 对出站数据进行压缩处理
     *
     * 当数据长度超过阈值时进行压缩，否则直接传输原始数据
     *
     * @param ctx 网络通道处理上下文
     * @param msg 需要压缩的数据
     * @param out 压缩后的数据输出列表
     * @throws Exception 压缩过程中可能抛出的异常
     */
    override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf?, out: MutableList<Any>) {
        //
        // 别问我为啥注释少，GlowStone作者不爱写注释跟我有啥关系？
        //

        val prefixBuf = ctx.alloc().buffer(5)
        val contentsBuf: ByteBuf

        val length = msg!!.readableBytes()
        if (length >= threshold) {
            // message should be compressed
            val index = msg.readerIndex()

            val sourceData = ctx.alloc().heapBuffer(length)
            msg.readBytes(sourceData)
            deflater.setInput(sourceData.array(), sourceData.arrayOffset() + sourceData.readerIndex(), length)
            deflater.finish()

            val compressedData = ctx.alloc().heapBuffer(length)
            val compressedLength = deflater.deflate(
                compressedData.array(),
                compressedData.arrayOffset() + compressedData.writerIndex(),
                compressedData.writableBytes(), Deflater.SYNC_FLUSH
            )

            deflater.reset()
            sourceData.release()

            if (compressedLength == 0) {
                // compression failed in some weird way
                compressedData.release()
                throw EncoderException("Failed to compress message of size $length")
            } else if (compressedLength >= length) {
                // compression increased the size. threshold is probably too low
                // send as an uncompressed packet
                compressedData.release()
                ByteBufUtils.writeVarInt(prefixBuf, 0)
                msg.readerIndex(index)
                msg.retain()
                contentsBuf = msg
            } else {
                // all is well
                ByteBufUtils.writeVarInt(prefixBuf, length)
                contentsBuf = Unpooled.wrappedBuffer(
                    compressedData.array(),
                    compressedData.arrayOffset() + compressedData.readerIndex(),
                    compressedLength
                )
                compressedData.release()
            }
        } else {
            // message should be sent through
            ByteBufUtils.writeVarInt(prefixBuf, 0)
            msg.retain()
            contentsBuf = msg
        }

        out.add(Unpooled.wrappedBuffer(prefixBuf, contentsBuf))
    }

    /**
     * 对入站数据进行解压处理
     *
     * 检查数据是否被压缩，如果已压缩则进行解压操作
     *
     * @param ctx 网络通道处理上下文
     * @param msg 需要解压的数据
     * @param out 解压后的数据输出列表
     * @throws Exception 解压过程中可能抛出的异常
     */
    override fun decode(ctx: ChannelHandlerContext, msg: ByteBuf?, out: MutableList<Any>) {
        val index = msg!!.readerIndex()
        val uncompressedSize: Int = ByteBufUtils.readVarInt(msg)
        if (uncompressedSize == 0) {
            // message is uncompressed
            val length = msg.readableBytes()
            if (length >= threshold) {
                // invalid
                throw DecoderException(
                    ("Received uncompressed message of size " + length + " greater than threshold "
                            + threshold)
                )
            }

            val buf = ctx.alloc().buffer(length)
            msg.readBytes(buf, length)
            out.add(buf)
        } else {
            // message is compressed
            val sourceData = ByteArray(msg.readableBytes())
            msg.readBytes(sourceData)
            inflater.setInput(sourceData)

            val destData = ByteArray(uncompressedSize)
            val resultLength = inflater.inflate(destData)
            inflater.reset()

            if (resultLength == 0) {
                // might be a leftover from before compression was enabled (no compression header)
                // uncompressedSize is likely to be < threshold
                msg.readerIndex(index)
                msg.retain()
                out.add(msg)
            } else if (resultLength != uncompressedSize) {
                throw DecoderException(
                    ("Received compressed message claiming to be of size " + uncompressedSize
                            + " but actually " + resultLength)
                )
            } else {
                out.add(Unpooled.wrappedBuffer(destData))
            }
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        logger.warn("一个错误在压缩或者解压缩数据包时发生了", cause)
    }

    companion object {
        private const val COMPRESSION_LEVEL = Deflater.DEFAULT_COMPRESSION
    }
}