package cn.zc.app

import com.google.gson.JsonParser
import net.kyori.adventure.nbt.*

fun main() {
    val tag = CompoundBinaryTag.builder()
        .putString("standard", "value")
        .putString("complex", "weird, isn't it: huh")
        .putString("quoted", "quo\"ted")
        .putString("comp:lex \"key", "let's go")
        .put(
            "listed", ListBinaryTag.builder(BinaryTagTypes.STRING)
                .add(StringBinaryTag.stringBinaryTag("one"))
                .add(StringBinaryTag.stringBinaryTag("two"))
                .add(StringBinaryTag.stringBinaryTag("three"))
                .build()
        )
        .build()

    TagStringIO.tagStringIO().asString(tag).also {
        println(it)
    }.let {
        JsonParser.parseString(it).asJsonObject
    }.also {
        println(it)
    }
}
