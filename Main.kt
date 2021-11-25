import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import org.intellij.lang.annotations.Language

private const val KEY_CONSOLIDATED_WEATHER = "consolidated_weather"
private const val KEY_NAME_OF_CITY = "title"
private const val KEY_MIN_TEMPERATURE = "min_temp"
private const val KEY_MAX_TEMPERATURE = "max_temp"
private const val KEY_THE_TEMPERATURE = "the_temp"
private const val KEY_WIND_SPEED = "wind_speed"
private const val KEY_CREATED_SPEED = "created"

//
/*
*
* */


suspend fun main() {
    requestToWeather("Moscow")
}

suspend fun requestToWeather(city: String) {
    val client = HttpClient(CIO)
    val response: HttpResponse =
        client.get("https://api.openweathermap.org/data/2.5/weather?q=Moscow&units=metric&lang=ru&appid=5ba6d4e9b64a6355a8c8222fda310aeb")
    client.close()

    val jsonParser = JsonParser()
    val jObject = jsonParser.parse(java.lang.String.valueOf(String(response.receive()))) as JsonObject

    val jsonArrWeather = jObject.getAsJsonArray("weather")
    val jsonArrMain = jObject.getAsJsonObject("main")
    val jsonArrWind = jObject.getAsJsonObject("wind")

    val weather = jsonArrWeather.get(0).asJsonObject["main"]
    val description = jsonArrWeather.get(0).asJsonObject["description"]

    val main = jsonArrMain.asJsonObject["temp"]
    val feelsLike = jsonArrMain.asJsonObject["feels_like"]

    val speed = jsonArrWind.asJsonObject["speed"]

    println(weather)
    println(description)

    println(main)
    println(feelsLike)

    println(speed)
    //val city = jObject[KEY_NAME_OF_CITY].toString()
    //val minTemp = data[KEY_MIN_TEMPERATURE].toString().toDouble()


}

fun selectLanguage(language: String): String {
    return ""
}

/*suspend fun main() {
    val client = HttpClient(CIO)
    val response: HttpResponse =
        client.get("https://api.openweathermap.org/data/2.5/weather?q=London&appid=5ba6d4e9b64a6355a8c8222fda310aeb")
    val responseMoscow: HttpResponse =
        client.get("https://api.openweathermap.org/data/2.5/weather?q=Moscow&units=metric&lang=ru&appid=5ba6d4e9b64a6355a8c8222fda310aeb")

    val str: StringBuilder = StringBuilder()
    val responseThird: HttpResponse =
        client.get("https://api.openweathermap.org/data/2.5/weather?q=Moscow&lang=nl&appid=5ba6d4e9b64a6355a8c8222fda310aeb")
        {
            runBlocking {
                client.get<HttpStatement>("https://api.openweathermap.org/data/2.5/weather?q=Moscow&lang=nl&appid=5ba6d4e9b64a6355a8c8222fda310aeb")
                    .execute { httpResponse ->
                        val channel: ByteReadChannel = httpResponse.receive()
                        while (!channel.isClosedForRead) {
                            val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
                            while (!packet.isEmpty) {
                                val bytes = packet.readBytes()
                                str.append(bytes.toString())
                                println("Received ${str.length} bytes from ${httpResponse.contentLength()}")
                            }
                        }
                    }
            }
        }

    val responseTest: HttpResponse =
        client.get("https://www.metaweather.com/api/location/2122265")

    println(response.status)
    val stringBody: String = response.receive()
    val stringBodyMoscow: String = responseMoscow.receive()
    val stringThird: String = responseThird.receive()

    println(stringBody)
    println(stringBodyMoscow)
    println(stringThird)
    println(str)

    val jsonParser = JsonParser()
    val jObject = jsonParser.parse(java.lang.String.valueOf(String(responseTest.receive()))) as JsonObject
    val jsonArr = jObject.getAsJsonArray(KEY_CONSOLIDATED_WEATHER)
    val data = jsonArr[0].asJsonObject

    val city = jObject[KEY_NAME_OF_CITY].toString()
    val minTemp = data[KEY_MIN_TEMPERATURE].toString().toDouble()
    val maxTemp = data[KEY_MAX_TEMPERATURE].toString().toDouble()
    val theTemp = data[KEY_THE_TEMPERATURE].toString().toDouble()
    val windSpeed = data[KEY_WIND_SPEED].toString().toDouble()
    val created = data[KEY_CREATED_SPEED].toString()
    client.close()

    println(city)
    println(minTemp)
    println(maxTemp)
    println(theTemp)
    println(windSpeed)
    println(created)
}*/

