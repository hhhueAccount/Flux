package cn.zc

fun main() {
    val server = NetworkServer()
    server.launch()
//    server.stop()
    LogicCore.setupListener()
    EarlyRegistryLoader.loadAll()
}
