package cn.zc.handler

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerAdapter

/**
 * 空处理器对象，作为处理器链中的占位符。
 *
 * 当某些功能（如加密、压缩）暂时不需要实现时，使用此空处理器保持处理器链的结构。
 * 继承自ChannelHandlerAdapter，不执行任何操作。
 */
@ChannelHandler.Sharable
object Empty : ChannelHandlerAdapter();