package cn.zc

import cn.zc.PluginMessages.addListener
import cn.zc.extension.Identifier
import cn.zc.handler.ListPingHandler
import cn.zc.handler.LoginHandler
import cn.zc.handler.PluginMessageHandler
import com.google.common.io.ByteArrayDataInput
import org.apache.logging.log4j.kotlin.logger
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
//                logger.info(String(input.readUTF()))
            }
        }

        addListener(Identifier(value = "brand"), Listener())
    }
}