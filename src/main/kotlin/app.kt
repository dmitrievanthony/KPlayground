import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType
import io.ktor.util.KtorExperimentalAPI
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@UseExperimental(ExperimentalTime::class)
fun main() {
    val clients = mutableListOf<HttpClient>()

    val singleShotTime = measureTime {
        clients += HttpClient(OkHttp) {
            install(JsonFeature) {
                serializer = GsonSerializer()
                @UseExperimental(KtorExperimentalAPI::class)
                acceptContentTypes += ContentType("application", "json+hal")
            }
        }
    }

    val averageTime = measureTime {
        repeat(1000) {
            clients += HttpClient(OkHttp) {
                install(JsonFeature) {
                    serializer = GsonSerializer()
                    @UseExperimental(KtorExperimentalAPI::class)
                    acceptContentTypes += ContentType("application", "json+hal")
                }
            }
        }
    }

    println("HttpClient: Single shot time: ${singleShotTime.inMilliseconds} ms/op")
    println("HttpClient: Average time: ${averageTime.inMilliseconds / 1000} ms/op")
}