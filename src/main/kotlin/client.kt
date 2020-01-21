import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.*

fun main(args: Array<String>) {
    runBlocking {
        val socket = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp().connect("127.0.0.1", 2323)
        val input = socket.openReadChannel()
        val output = socket.openWriteChannel(autoFlush = true)

        output.write { it.put("hello\r\n".toByteArray()) }
        val response = input.readUTF8Line(1024)
        println("Server said: '$response'")
    }
}
