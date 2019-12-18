import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.cookies.AcceptAllCookiesStorage
import io.ktor.client.features.cookies.HttpCookies
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.client.response.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.sessions.sessions
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

data class MySession(val counter: Int)

fun main() = runBlocking {
    val server = embeddedServer(Netty, port = 8081) {
        install(Sessions) {
            cookie<MySession>("SESSION")
        }

        routing {
            get("/") {
                if (call.sessions.get("SESSION") == null) {
                    call.sessions.set("SESSION", MySession(42))
                }

                call.respond(HttpStatusCode.OK)
            }
        }
    }

    server.start()

    val client = HttpClient(OkHttp) {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.HEADERS
        }

        install(HttpCookies) {
            storage = AcceptAllCookiesStorage()
        }
    }

    try {
        println("===== FIRST REQUEST =====")
        client.get<HttpResponse>("http://localhost:8081")
        println("=====")
        delay(1000)

        println("===== SECOND REQUEST =====")
        client.get<HttpResponse>("http://localhost:8081")
        println("=====")
        delay(1000)

        println("===== END =====")
    } finally {
        client.close()
    }
}