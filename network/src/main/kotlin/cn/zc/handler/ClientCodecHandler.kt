package cn.zc.handler

import cn.zc.Packets
import cn.zc.extension.minecraft
import cn.zc.extension.readVarInt
import cn.zc.extension.writeVarInt
import cn.zc.packet.ErrorPacket
import cn.zc.packet.Packet
import cn.zc.registry.ClientBound
import cn.zc.registry.ServerBound
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec
import org.apache.logging.log4j.kotlin.logger

class ClientCodecHandler : MessageToMessageCodec<ByteBuf, Packet>() {
    /**
     * 将[Packet]对象编码[ByteBuf]
     *
     * 向数据包写入ID标识，并将数据包内容序列化为字节流，
     * 同时触发服务器发送数据包事件
     *
     * @param ctx 网络通道处理上下文
     * @param packet 需要编码的数据包对象
     * @param out 编码后的字节流输出列表
     */
    override fun encode(ctx: ChannelHandlerContext, packet: Packet, out: MutableList<Any>) {
        val clientConnection = ctx.channel().minecraft()
        // 申请一个新的ByteBuf，用于向下传递，进行进一步处理
        val buffer = ctx.alloc().buffer()

        // 针对传入的packet对象的类型获取其对应的ID
        val id = ServerBound.state(clientConnection.state).getId(packet::class)
        // 获取id失败，取消发送数据
        if (id == -2) return

        // 写入包ID
        buffer.writeVarInt(id)

        // 写入包数据
        //
        // 每一个Packet的子类都会实现serialize()，那么我们现在直接去调用即可
        packet.serialize(buffer)
        logger.trace("[O](${ctx.channel().remoteAddress()}) $packet")

        buffer.markReaderIndex()
        buffer.resetReaderIndex()
        out.add(buffer)
    }

    /**
     * 将网络字节流解码为数据包对象
     *
     * 从字节流中读取数据包ID，并根据ID创建对应的数据包对象，
     * 然后将剩余的字节数据反序列化到数据包对象中
     *
     * @param ctx 网络通道处理上下文
     * @param trimmedBuffer 网络字节流缓冲区
     * @param out 解码后的数据包对象输出列表
     */
    override fun decode(ctx: ChannelHandlerContext, trimmedBuffer: ByteBuf, out: MutableList<Any>) {
        trimmedBuffer.markReaderIndex()
        val clientConnection = ctx.channel().minecraft()

        // 读取包ID
        val id = trimmedBuffer.readVarInt()
        // 根据ID对应的数据读取逻辑来初始化Packet对象
        val packet = ClientBound.state(clientConnection.state).deserialize(id, trimmedBuffer)
        // 读取失败了，直接取消接下来的逻辑的推进，防止出现事故
        if (packet == ErrorPacket) return

        logger.trace("[I](${ctx.channel().remoteAddress()}) $packet")
        // 绑定发送者
        packet.from = ctx.channel().minecraft()
        // 分发数据包到处理逻辑处，进行处理
        Packets.post(packet)

        // 发生了错误！
        // 一般这种情况都意味着数据包读取逻辑不对劲，写错了！
        if (trimmedBuffer.readableBytes() != 0) {
            logger.trace("${packet}出现${trimmedBuffer.readableBytes()}字节未读取的数据，读取逻辑是否有疏漏?")
            trimmedBuffer.resetReaderIndex()
            return
        }

        out.add(packet)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        logger.error("一个错误在客户端解析或者发送数据包时发生了", cause)
    }
}