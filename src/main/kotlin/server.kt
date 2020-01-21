import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.*

/*
 * Copyright 2014-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

fun main(args: Array<String>) {
    runBlocking {
        val server = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp().bind("127.0.0.1", 2323)

        while (true) {
            val socket = server.accept()

            launch {

                val input = socket.openReadChannel()
                val output = socket.openWriteChannel(autoFlush = true)

                try {
                    while (true) {
                        val line = input.readUTF8Line(1024)

                        println("Get: $line")
                        output.write { it.put("$line\r\n".toByteArray()) }
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    socket.close()
                }
            }
        }
    }
}
