import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.core.*
import java.util.*

private const val API_KEY = "5ba6d4e9b64a6355a8c8222fda310aeb"
private const val URL_PART_FIRST = "https://api.openweathermap.org/data/2.5/weather?q="
private const val URL_PART_SECOND = "&units=metric&lang="
private const val URL_PART_THIRD = "&appid="

private const val KEY_NAME = "name"
private const val KEY_WEATHER = "weather"
private const val KEY_MAIN = "main"
private const val KEY_WIND = "wind"

private const val KEY_FEATURES_MAIN = "main"
private const val KEY_FEATURES_DESCRIPTION = "description"
private const val KEY_FEATURES_TEMP = "temp"
private const val KEY_FEATURES_FEELS = "feels_like"
private const val KEY_FEATURES_SPEED = "speed"


suspend fun main() {
    val dataWeather = parseData(requestToWeather("Moscow", "en"))
    println(dataWeather)
}

private suspend fun requestToWeather(city: String, language: String): JsonObject {
    val client = HttpClient(CIO)
    val response: HttpResponse =
        client.get(URL_PART_FIRST + city.lowercase(Locale.getDefault()) + URL_PART_SECOND + language + URL_PART_THIRD + API_KEY)
    client.close()

    val jsonParser = JsonParser()
    return jsonParser.parse(java.lang.String.valueOf(String(response.receive()))) as JsonObject
}

fun parseData(jObject: JsonObject): WeatherObject {
    val jsonArrWeather = jObject.getAsJsonArray(KEY_WEATHER)
    val jsonArrMain = jObject.getAsJsonObject(KEY_MAIN)
    val jsonArrWind = jObject.getAsJsonObject(KEY_WIND)

    val name = jObject.getAsJsonPrimitive(KEY_NAME)
    val weather = jsonArrWeather.get(0).asJsonObject[KEY_FEATURES_MAIN]
    val description = jsonArrWeather.get(0).asJsonObject[KEY_FEATURES_DESCRIPTION]
    val main = jsonArrMain.asJsonObject[KEY_FEATURES_TEMP]
    val feelsLike = jsonArrMain.asJsonObject[KEY_FEATURES_FEELS]
    val speed = jsonArrWind.asJsonObject[KEY_FEATURES_SPEED]

    return WeatherObject(
        name.toString(),
        weather.toString(),
        main.toString(),
        description.toString(),
        feelsLike.toString(),
        speed.toString()
    )
}

fun selectLanguage(language: String): String {
    return ""
}

fun menu() {

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

