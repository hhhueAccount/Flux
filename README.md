# 📚 Flux

![GitHub License](https://img.shields.io/github/license/hhhueAccount/Flux)
![Static Badge](https://img.shields.io/badge/minecraft%20rewrite-blue)

[中文说明](./README_zh_CN.md)

Flux is a Kotlin-rewritten Minecraft server. Like other similar open-source projects,
it does not depend on Minecraft code written by Mojang,
and is essentially an independent project. When it's necessary to recreate the original Minecraft program logic or data structures,
Flux will consider incorporating and simplifying the relevant code from the original Minecraft as much as possible.

Considering that some traditional server development APIs have significant advantages, such as having already fostered a very large developer community
Flux plans to implement the excellent PaperAPI. Additionally, to provide developers with more room for innovation, Flux intends to expose some APIs,
or even expose all code content to developers. However, this is not the current (March 2026) focus.

Flux is developed across several modules, each responsible for a set of features with a single, clear objective.
I expect each module to be usable by developers as a brand-new dependency library.
Therefore, in the future, Flux plans to build each module separately and upload them to a Maven repository.

### Features
- Will support `PaperAPI`;
- Lightweight codebase;
- Will expose more flexible APIs;
- Eliminates dependency on `net.minecraft.server` development;
- Plans to write more comprehensive feature documentation;
- Will refactor some bloated game logic and game data structures.

### Goals

To implement a brand-new Minecraft server, not only rewriting its network stack but also its core game logic.
Flux cannot fully replicate all the game logic of the original Minecraft;
that is a task for a commercial game development team,
not for an individual or a group of volunteer developers in an open-source community.
Flux merely proposes a more maintainable and robust design scheme for the core game logic code.
On this basis, it would be even better if it could become a superior platform for Minecraft server development;
if not, that's perfectly understandable.

### Network Module Supplementary Explanation

You can use it to communicate with the vanilla Minecraft client (using the `NetworkServer` class),
and also to communicate with the vanilla Minecraft server (using the `NetworkClient` class).

The usage of these two classes is very similar. For ease of explanation, let's take `NetworkClient` as an example. You first need to create a `NetworkClient` object,
then call its `launch()` method to manually initialize the internally managed `Netty` server or client. The initialization process is thread-synchronous
and does not support asynchrony. Example:

```kotlin
val client = NetworkClient(host = "play.hypixel.net")
client.launch()
```

Sending packets is also simple. You can continue writing the following code snippet:

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

Packets must be sent synchronously here; you cannot call the asynchronous send method (`send()`),
otherwise thread synchronization issues may occur. As you can see,
sending a packet each time requires creating the packet object and filling in the parameters.
This is Minecraft's handshake packet, used to inform the server.
For more details, please refer to: [wiki.vg](https://minecraft.wiki/w/Java_Edition_protocol/Packets#Handshake).

Next, let's introduce how to listen for incoming packets.
You need to create a `class` or an `object`.
Using an `object` is recommended here because it's a singleton.

The listener method needs to be annotated with `@Subscribe`, and the method parameter should specify the packet type to listen for.
When sending packets within the listener method, you don't need to use the synchronous send method,
as this method is ultimately called by `Netty`, and `Netty` handles thread synchronization for us.

Finally, register this listener, that's it:

```kotlin
Packets.register(TestListner)
```

### Current Development Progress

**Completed:** Network packet read/write functionality for the `Status` and `Login` communication stages in the Minecraft network protocol,
and partial logic implementation for the `Configuration` stage.

**In Progress:** The communication logic for the Configuration stage heavily relies on Minecraft's registry system,
so the design of Flux's registry system is currently underway.

**Planned in Order:** Allow clients to correctly enter the server's test world;
design and implement a new chunk loading system and chunk sending system;
implement minor vanilla features like team systems and scoreboard systems;
game logic update system (tick system).