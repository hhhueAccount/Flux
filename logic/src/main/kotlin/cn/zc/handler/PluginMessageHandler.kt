package cn.zc.handler

import cn.zc.PluginMessages
import cn.zc.packet.serverbound.configuration.ServerPluginMessagePacket
import com.google.common.eventbus.Subscribe

object PluginMessageHandler {
    @Subscribe
    fun onReceiveMessage(pluginMessagePacket: ServerPluginMessagePacket) {
        PluginMessages.postMessage(pluginMessagePacket.identifier, pluginMessagePacket.payload)
    }
}