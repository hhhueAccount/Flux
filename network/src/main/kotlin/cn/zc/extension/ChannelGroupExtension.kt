package cn.zc.extension

import io.netty.channel.ChannelId
import io.netty.channel.group.ChannelGroup

fun ChannelGroup.minecraft(id: ChannelId) = find(id).minecraft()