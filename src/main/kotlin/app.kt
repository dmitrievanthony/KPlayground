import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.response.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val server = embeddedServer(Netty, port = 8081) {
        routing {
            post("/") {
                call.receiveMultipart().forEachPart {
                    try {
                        if (it is PartData.FileItem) {
                            val array = ByteArray(1024 * 1024)
                            it.provider().readFully(array, 0, 1024 * 1024)
                        }
                    } finally {
                        it.dispose()
                    }
                }
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    server.start()

    testEngine(OkHttp)
    testEngine(Android)
    testEngine(CIO)
}

suspend fun testEngine(engineFactory: HttpClientEngineFactory<*>) {
    println("Testing $engineFactory...")

    val client = HttpClient(engineFactory)
    try {
        repeat(16) {
            val result = client.post<HttpResponse>("http://127.0.0.1:8081/") {
                body = MultiPartFormDataContent(formData {
                    append("file", ByteArray(1024 * 1024), Headers.build {
                        append(HttpHeaders.ContentDisposition,
                            """form-data; name="file"; filename="test.png"""")
                    })
                })
            }
            check(result.status == HttpStatusCode.OK)
        }
    } finally {
        client.close()
    }

    println("Testing $engineFactory completed successfully.")
}