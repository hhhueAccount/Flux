package cn.zc

import cn.zc.PluginMessages.addListener
import cn.zc.handler.ListPingHandler
import cn.zc.handler.LoginHandler
import cn.zc.handler.PluginMessageHandler
import cn.zc.resource.Identifier
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
object LogicCore {

    @ApiStatus.Internal
    @ApiStatus.Experimental
    fun setupListener() {
        Packets.register(ListPingHandler)
        Packets.register(LoginHandler)
        Packets.register(PluginMessageHandler)

        class Listener : MessageListener {
            override fun onReceive(input: ByteArray) {
            }
        }

        addListener(Identifier(value = "brand"), Listener())
    }
}