# 📚 Flux

![GitHub License](https://img.shields.io/github/license/hhhueAccount/Flux)
![Code Size](https://img.shields.io/github/languages/code-size/hhhueAccount/Flux)
![Line of Code](https://www.aschey.tech/tokei/github.com/hhhueAccount/Flux)
![Static Badge](https://img.shields.io/badge/minecraft%20rewrite-blue)

Flux是一个基于kotlin重写的Minecraft服务端，与其他类似的开源项目一样，它不依赖Mojang写的Minecraft代码，
相对来讲，是一个基本上独立的项目。 在一些不得不重现原版Minecraft的程序逻辑或数据结构时，
Flux才会考虑接受并尽可能精简这些来自原版Minecraft的相关代码。

考虑到一些传统的服务器开发API拥有一些非常巨大的优势，譬如已经收获了一个非常庞大的开发社区，这还仅仅是其中之一。
所以，Flux考虑实现其中较为优秀的PaperAPI，同时，为了给予开发者更多的发挥空间，Flux准备在此基础上暴露一些API，
或者干脆把所有代码内容都暴露给开发者。当然，这不是现在（2026年3月）的工作。

Flux分出了若干个模块来进行开发，每一个模块负责一系列目标指向单一的功能。我期望每一个模块都允许开发者把它当作一个全新的依赖库来使用，
所以，在未来，Flux考虑为每一个模块做一个单独的构建，并上传至Maven仓库。

### 目标

实现一个全新的Minecraft服务器，不仅仅是重写其网络堆栈，同时需要重写其游戏核心。Flux做不到完全复现原版Minecraft的全部游戏逻辑，
这是一个商业游戏团队才能完成的任务，而不是个人或者是一群开源社区的志愿开发者们可以完成的。Flux仅仅为游戏核心逻辑代码的设计提出一种更加容易维护，
同时更加健壮的方案，在此基础上，若能成为一种更好的Minecraft游戏服务器开发平台是更好的，如果不能做到，是情理之内的。

### 特性

- 将会支持`PaperAPI`；
- 代码轻量化；
- 将会暴露更多灵活的API；
- 推倒`net.minecraft.server`开发；
- 预计会编写更多的功能说明文档；
- 将会重构一些臃肿的游戏逻辑和游戏数据结构。

### Network模块辅助理解说明

你可以用它来向原版客户端来进行通讯（使用`NetworkServer`类），同时也可以用它来向原版服务端进行通讯（使用`NetworkClient`类）。

两个类的使用方式非常类似，为了方便说明，这里以`NetworkClient`为例。你需要先创建一个`NetworkClient`对象，
然后调用其中的`launch()`方法来手动初始化在其内部维护的`Netty`服务端或者是客户端，初始化的过程是线程同步的，
不支持异步。示例如下：

```kotlin
val client = NetworkClient(host = "play.hypixel.net")
client.launch()
```

发送数据包也很简单，你可以顺着刚刚的代码继续往下写这么一段：

```kotlin
client.sendSync(
    IntentionPacket(
        772,
        "localhost",
        25565,
        Intent.STATUS
    )
)
```

这里一定要同步发送数据包，不能调用异步发送方法（`send()`），否则会出现线程同步问题。可以看到，每次发送数据包都需要创建数据包，并填好参数。
这是Minecraft的握手包，用于提示服务端，具体说明请参阅：[wiki.vg](https://minecraft.wiki/w/Java_Edition_protocol/Packets#Handshake)。

下面介绍如何监听数据包的接受。你需要创建一个类，或者一个`object`，这里推荐`object`，因为它是单例的。

```kotlin
object TestListener {
    @Subscribe
    fun onStatus(statusResponsePacket: StatusResponsePacket) {
        // your code ...
        logger.info("i got it!")
    }
}
```

最后，注册这个监听器即可：

```kotlin
Packets.register(TestListner)
```

监听方法需要用`@Subscribe`注解，方法参数填写需要监听的数据包。在监听方法内发送数据包不必使用同步发送方法，该方法最终由`Netty`调用，
`Netty`为我们做好了线程同步。

### 现在的开发进度

**已完成：** Minecraft网络通讯协议中`Status`、`Login`通讯阶段的网络数据读写功能，实现了`Configuration`阶段的部分逻辑。

**正在进行：** `Configuration`阶段的通讯逻辑非常依赖Minecraft的注册表系统，所以目前正在设计Flux的注册表系统。

**未来将要按顺序进行：** 让客户端正确进入服务器的测试世界；设计并编写新的区块加载系统、区块发送系统；实现原版细枝末节的小内容，如队伍系统、计分系统；
游戏逻辑更新系统（tick系统）。