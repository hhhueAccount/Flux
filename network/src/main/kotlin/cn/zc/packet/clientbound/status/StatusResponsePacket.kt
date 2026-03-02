package cn.zc.packet.clientbound.status

import cn.zc.extension.readUTF8
import cn.zc.extension.writeUTF8
import cn.zc.packet.clientbound.ClientBoundPacket
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.netty.buffer.ByteBuf

data class StatusResponsePacket(val jsonObject: JsonObject) : ClientBoundPacket() {
    constructor(byteBuf: ByteBuf) : this(
        JsonParser.parseString(byteBuf.readUTF8()).asJsonObject
    )

    override fun serialize(byteBuf: ByteBuf) {
        byteBuf.writeUTF8(jsonObject.toString())
    }
}
