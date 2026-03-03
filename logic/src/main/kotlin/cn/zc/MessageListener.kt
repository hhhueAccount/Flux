package cn.zc

import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
@FunctionalInterface
interface MessageListener {
    fun onReceive(input: ByteArray)
}