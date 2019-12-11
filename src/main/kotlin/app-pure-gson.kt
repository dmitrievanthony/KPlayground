import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@UseExperimental(ExperimentalTime::class)
fun main() {
    val clients = mutableListOf<Gson>()

    val singleShotTime = measureTime {
        clients += GsonBuilder().create()
    }

    val averageTime = measureTime {
        repeat(1000) {
            clients += GsonBuilder().create()
        }
    }

    println("Gson: Single shot time: ${singleShotTime.inMilliseconds} ms/op")
    println("Gson: Average time: ${averageTime.inMilliseconds / 1000} ms/op")
}