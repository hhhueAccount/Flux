package cn.zc.packet

import kotlinx.serialization.Serializable
import org.jetbrains.annotations.ApiStatus

/**
 * 占位数据包，用于占位那些在`Minecraft`原版中存在而实际上没有任何发送逻辑、处理逻辑的数据包，
 * 以此保证在数据包注册表中，数据包ID和数据包本身的映射关系，
 * 进而进而间接保证发送给对端的数据包能被游戏程序以正确的读取逻辑正确读取。
 *
 * 以上，该数据包不具备任何读取与写入逻辑。
 */
@ApiStatus.Internal
@Serializable
class PlaceHolerPacket : Packet()