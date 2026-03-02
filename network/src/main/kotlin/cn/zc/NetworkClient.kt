package cn.zc

import cn.zc.extension.minecraft
import cn.zc.handler.ClientCodecHandler
import cn.zc.handler.Empty
import cn.zc.handler.FramingHandler
import cn.zc.packet.Packet
import com.google.common.base.MoreObjects
import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.MultiThreadIoEventLoopGroup
import io.netty.channel.nio.NioIoHandler
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.timeout.ReadTimeoutHandler
import org.apache.logging.log4j.kotlin.logger

class NetworkClient(val port: Int = 25565, val host: String = "localhost") {
    /**
     * Worker线程组，负责处理已建立连接的I/O操作（读写数据）。
     * 使用4个线程处理客户端连接的I/O任务，实现并发处理多个连接。
     * 线程名称以"client#"为前缀，标识客户端I/O处理线程。
     */
    private val group = MultiThreadIoEventLoopGroup(
        4,
        ThreadFactoryBuilder().setNameFormat("client#%d").build(),
        NioIoHandler.newFactory()
    )

    /**
     * 内部维护的会话对象
     *
     * 会在`launch()`方法中被初始化。用来发送
     */
    lateinit var session: Session

    fun launch() {
        logger.info("正在开启客户端网络模块...")

        val future = Bootstrap()
            .group(group)
            .channel(NioSocketChannel::class.java)
            .handler(object : ChannelInitializer<NioSocketChannel>() {
                override fun initChannel(ch: NioSocketChannel) {
                    // 分配处理器
                    ch.pipeline()
                        ?.addLast("timeout", ReadTimeoutHandler(30))
                        ?.addLast("encryption", Empty)
                        ?.addLast("framing", FramingHandler())
                        ?.addLast("compression", Empty)
                        ?.addLast("codec", ClientCodecHandler())
                }

                /**
                 * bugfix:
                 * 防止netty初始化时出现报错不进行日志输出
                 */
                override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
                    logger.error("一个错误在初始化客户端连接时发生了", cause)
                }
            })
            .connect(host, port)
            .sync()
        session = future
            .channel()
            .minecraft()

        if (future.isSuccess) {
            // 启动成功
            logger.info("✅ 客户端网络模块成功连接到 $host:$port")
            logger.debug("会话详情：$session")
        } else {
            // 启动失败
            // 输出日志并且关闭网络服务
            logger.error("❌ 客户端网络服务模块启动失败：无法绑定到 $host:$port")
            stop()
        }
    }

    /**
     * 停止Minecraft网络客户端。
     *
     * 注意：`shutdownGracefully()`方法会等待当前任务完成后再关闭。
     */
    fun stop() {
        logger.info("正在停止客户端网络服务模块...")

        session.disconnect()
        group.shutdownGracefully()

        logger.info("$host:$port 上的网络客户端已经被关闭")
    }

    /**
     * 推进连接状态到下一个阶段。
     *
     * 典型的Minecraft连接状态流转：
     * `HANDSHAKE → STATUS → LOGIN → PLAY`
     * 如果推进到`PLAY`阶段再尝试推进，那么连接始终停留于`PLAY`阶段
     *
     * 每个状态对应不同的协议处理逻辑。
     *
     * @see ConnectionState.next 获取下一个状态
     */
    fun nextState() {
        session.nextState()
    }

    /**
     * 异步发送数据包
     */
    fun send(packet: Packet) {
        session.send(packet)
    }

    /**
     * 同步发送数据包
     */
    fun sendSync(packet: Packet) {
        session.sendSync(packet)
    }

    /**
     * 异步发送多个数据包
     *
     * 先进行写入，然后再统一flush
     */
    fun send(vararg packets: Packet) {
        session.send(*packets)
    }

    /**
     * 同步发送多个数据包
     *
     * 先进行写入，然后再统一flush
     */
    fun sendSync(vararg packets: Packet) {
        session.sendSync(*packets)
    }

    override fun toString() =
        MoreObjects.toStringHelper(this)
            .add("host", host)
            .add("port", port)
            .omitEmptyValues()
            .omitNullValues()
            .toString()
}