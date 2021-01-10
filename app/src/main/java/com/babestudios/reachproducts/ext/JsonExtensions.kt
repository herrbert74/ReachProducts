package com.babestudios.reachproducts.ext

import java.io.IOException
import java.io.InputStream

fun String.loadJson():String {
    val inputStream = ::String.javaClass.classLoader?.getResourceAsStream("$this.json")
    inputStream?.let {
        return readString(inputStream)
    } ?: run { return "" }
}

@Throws(IOException::class)
fun readString(stream: InputStream): String {
    return String(stream.readBytes(), Charsets.UTF_8)
}