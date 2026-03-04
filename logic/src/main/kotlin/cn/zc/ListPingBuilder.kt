package cn.zc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import org.jetbrains.annotations.Contract
import java.io.InputStream
import kotlin.io.encoding.Base64

/**
 * 这是一个用于辅助构建[cn.zc.packet.clientbound.status.StatusResponsePacket]`ListPing`回复内容的工具。
 * 构建结果是一个Json对象，结构示例如下，可供参考：
 *
 * ```json
 * {
 *     "version": {
 *         "name": "1.21.8",
 *         "protocol": 772
 *     },
 *     "players": {
 *         "max": 20,
 *         "online": 1,
 *         "sample": [
 *             {
 *                 "name": "thinkofdeath",
 *                 "id": "4566e69f-c907-48ee-8d71-d7ba5aa00d20"
 * 			}
 *         ]
 *     },
 *     "description": {
 *         "text": "Hello, world!"
 *     },
 *     "favicon": "data:image/png;base64,<data>",
 *     "enforcesSecureChat": false
 * }
 * ```
 *
 * 更多说明，请参阅：[wiki.vg](https://minecraft.wiki/w/Java_Edition_protocol/Server_List_Ping#Status_Response)
 */
object ListPingBuilder {

    @Contract(pure = true)
    fun build(
        version: String,
        protocolVersion: Int,
        maxOnline: Int = 20,
        currentOnline: Int = 0,
        players: List<SimplePlayerData> = emptyList(),
        description: Component = Component.empty(),
        favicon: InputStream? = null,
        enforcesSecureChat: Boolean = false
    ): JsonObject {
        val root = JsonObject()

        // ###### Version ######
        val versionJson = JsonObject()
        root.add("version", versionJson)
        versionJson.addProperty("name", version)
        versionJson.addProperty("protocol", protocolVersion)

        // ###### Players ######
        val playersJson = JsonObject()
        root.add("players", playersJson)
        playersJson.addProperty("max", maxOnline)
        playersJson.addProperty("online", currentOnline)
        val sampleList = JsonArray()
        var sampleElement: JsonObject
        for (data in players) {
            sampleElement = JsonObject()
            sampleElement.addProperty("name", data.name)
            sampleElement.addProperty("id", data.uuid.toString())
        }
        playersJson.add("sample", sampleList)

        // ###### Description ######
        root.add(
            "description",
            JsonParser.parseString(JSONComponentSerializer.json().serialize(description))
        )

        // ###### Favicon ######
        if (favicon != null) {
            val faviconBase64 = toBase64(favicon)
            root.addProperty("favicon", "data:image/png;base64,$faviconBase64")
        }

        // ###### SecureChat ######
        root.addProperty("enforcesSecureChat", enforcesSecureChat)

        return root
    }

    /**
     * 在`Minecraft`的`ServerListPing`中，游戏服务器图标是通过传递图标的`Base64`字符串来实现的。
     * 所以我们需要一个具备对应功能的方法。该方法把一个[InputStream]转换为`Base64`字符串，实际使用中，
     * [InputStream]的指向内容务必是特定大小的图标。
     */
    @Contract(pure = true)
    fun toBase64(inputStream: InputStream) = Base64.encode(inputStream.readAllBytes())
}