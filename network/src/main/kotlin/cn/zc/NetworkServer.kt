package cn.zc

import cn.zc.extension.minecraft
import cn.zc.handler.Empty
import cn.zc.handler.FramingHandler
import cn.zc.handler.ServerCodecHandler
import com.google.common.base.MoreObjects
import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.MultiThreadIoEventLoopGroup
import io.netty.channel.nio.NioIoHandler
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.timeout.ReadTimeoutHandler
import org.apache.logging.log4j.kotlin.logger

/**
 * `Minecraft`网络服务器类，负责启动和管理`Minecraft`游戏服务器
 *
 * 这个类使用Netty框架实现高性能的网络通信，处理客户端连接、数据包编解码和网络I/O操作
 *
 * @property port 服务器监听的端口号
 * @property host 服务器绑定的主机地址
 *
 * @see ServerBootstrap Netty服务器启动类
 * @see MultiThreadIoEventLoopGroup Netty事件循环组
 */
class NetworkServer(val port: Int = 25565, val host: String = "localhost") {
    /**
     * Boss线程组，专门负责接受新的客户端连接
     */
    private val bossGroup = MultiThreadIoEventLoopGroup(
        2,
        ThreadFactoryBuilder().setNameFormat("accept#%d").build(),
        NioIoHandler.newFactory()
    )

    /**
     * Worker线程组，负责处理已建立连接的I/O操作
     */
    private val workerGroup = MultiThreadIoEventLoopGroup(
        4,
        ThreadFactoryBuilder().setNameFormat("client#%d").build(),
        NioIoHandler.newFactory()
    )

    /**
     * 存储所有客户端连接`Session`对象
     *
     * 可以用于获取在线人数，进行批量化操作等等
     */
    val clients: MutableSet<Session> = HashSet()

    /**
     * 服务器通道，表示与客户端的连接通道。
     *
     * 使用`lateinit`延迟初始化，在`launch()`方法中创建。
     */
    lateinit var session: Session

    /**
     * 启动`Minecraft`网络服务器。
     *
     * 该方法配置并启动Netty服务器，包括线程组配置、通道初始化和端口绑定。
     * 使用监听器检测绑定操作的成功与否，并提供详细的日志输出。
     */
    fun launch() {
        logger.info("开始启动网络服务...")

        // 传回一个future对象，用于判定服务器是否顺利启动
        val future = ServerBootstrap()
            .group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel::class.java)
            .childHandler(object : ChannelInitializer<NioSocketChannel>() {
                /**
                 * 客户端连接服务器时触发
                 *
                 * 可以处理一些连接逻辑
                 */
                override fun initChannel(ch: NioSocketChannel) {
                    ch.pipeline()
                        ?.addLast("timeout", ReadTimeoutHandler(30))
                        ?.addLast("encryption", Empty)
                        ?.addLast("framing", FramingHandler())
                        ?.addLast("compression", Empty)
                        ?.addLast("codec", ServerCodecHandler())

                    clients.add(ch.minecraft())

                    logger.info("[Connect] ${ch.remoteAddress()}")
                }

                /**
                 * bugfix:
                 * 防止netty初始化时出现报错不进行日志输出
                 */
                override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
                    logger.error("一个错误在初始化客户端连接时发生了", cause)
                }
            })
            .bind(host, port)
        // 这里进一步利用future对象获取封装好的session对象
        session = future
            .sync()
            .channel()
            .minecraft()

        // 在上述逻辑同步执行完毕后，判断是否成功启动
        if (future.isSuccess) {
            // 启动成功逻辑
            logger.info("✅ 网络服务模块已经在 $host:$port 上开启")
            logger.debug("服务器通道详情：$session")
            logger.debug("网络模块状态：$this")
        } else {
            // 启动失败逻辑
            //
            // 输出日志并且关闭网络服务
            logger.error("❌ 网络服务模块启动失败：无法绑定到 $host:$port")
            logger.debug("网络模块状态：$this")
            stop()
        }
    }

    /**
     * 停止当前`Minecraft`网络服务器。
     */
    fun stop() {
        logger.info("正在停止网络服务模块...")

        // 关闭服务器连接
        session.disconnect()
        // 关闭线程组
        bossGroup.shutdownGracefully()
        workerGroup.shutdownGracefully()

        logger.info("$host:$port 上的网络服务已经被关闭")
    }

    /**
     * 获取在线的连接数量。
     *
     * 并不是指在线玩家数量，有时候，客户端可能建立了连接，但是，
     * 还没有进入游戏，正式成为一名在线的玩家。只有服务器逻辑内核承认，它才会是一个玩家。
     */
    fun onlineNumber() = clients.size

    override fun toString(): String =
        MoreObjects.toStringHelper(this)
            .add("host", host)
            .add("port", port)
            .omitEmptyValues()
            .omitNullValues()
            .toString()
}