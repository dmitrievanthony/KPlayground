import okhttp3.OkHttpClient
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@UseExperimental(ExperimentalTime::class)
fun main() {
    val clients = mutableListOf<OkHttpClient>()

    val singleShotTime = measureTime {
        clients += OkHttpClient()
    }

    val averageTime = measureTime {
        repeat(1000) {
            clients += OkHttpClient()
        }
    }

    println("OkHttp: Single shot time: ${singleShotTime.inMilliseconds} ms/op")
    println("OkHttp: Average time: ${averageTime.inMilliseconds / 1000} ms/op")
}