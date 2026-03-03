package cn.zc.app

import cn.zc.LogicCore
import cn.zc.NetworkServer

fun main() {
    val server = NetworkServer()
    server.launch()
    LogicCore.setupListener()
}
