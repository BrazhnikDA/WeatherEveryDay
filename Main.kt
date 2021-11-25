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
    var inLanguage: String
    var inCity: String

    do {
        println(
            "Input \"1\" for get guid info" +
                    "Please select language: "
        )
        inLanguage = readLine().toString()
        if (inLanguage == "1")
            println(viewGuid())
    } while (inLanguage == "1")

    // Добавить более осознанную проверку, возможно запрос на сайт который вернёт ответ есть/не сущесвтует
    println("Please select city: ")
    inCity = readLine().toString()

    val language = selectLanguage(inLanguage)
    val city = selectCity(inCity)

    println(menu())
    val dataWeather = parseData(requestToWeather(city, language))
    println(dataWeather)
}

// Добавить запрос к переводчку (яндекс/гугл/сторонний сайт) для перевода фразы Weather на выбранный язык
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
        name.toString().replace("\"", ""),
        weather.toString().replace("\"", ""),
        main.asDouble,
        description.toString().replace("\"", "").substring(0, 1).uppercase() +
                description.toString().replace("\"", "").substring(1).lowercase(),
        feelsLike.asDouble,
        speed.asDouble
    )
}

fun selectCity(city: String): String {
    val resCity = StringBuilder()

    for (ch in city.lowercase()) {
        if (ch == ' ')
            resCity.append('-')
        else
            resCity.append(ch)
    }

    return resCity.toString()
}

fun selectLanguage(language: String): String {
    if (language.isNotEmpty())
        if (language.length == 2)
            return language.uppercase()

    return "en"
}

// Добавить к меню функциональности
fun menu(): String {
    val menu = StringBuilder()
    menu.append("Menu: \n")
    menu.append("1. View\n")
    menu.append("2. Change city\n")
    menu.append("3. Change language\n")
    menu.append("0. Exit this application\n")

    return menu.toString()
}

fun viewGuid(): String {
    return "All language: \n" +
            "af Afrikaans\n" +
            "al Albanian\n" +
            "ar Arabic\n" +
            "az Azerbaijani\n" +
            "bg Bulgarian\n" +
            "ca Catalan\n" +
            "cz Czech\n" +
            "da Danish\n" +
            "de German\n" +
            "el Greek\n" +
            "en English\n" +
            "eu Basque\n" +
            "fa Persian (Farsi)\n" +
            "fi Finnish\n" +
            "fr French\n" +
            "gl Galician\n" +
            "he Hebrew\n" +
            "hi Hindi\n" +
            "hr Croatian\n" +
            "hu Hungarian\n" +
            "id Indonesian\n" +
            "it Italian\n" +
            "ja Japanese\n" +
            "kr Korean\n" +
            "la Latvian\n" +
            "lt Lithuanian\n" +
            "mk Macedonian\n" +
            "no Norwegian\n" +
            "nl Dutch\n" +
            "pl Polish\n" +
            "pt Portuguese\n" +
            "pt_br Português Brasil\n" +
            "ro Romanian\n" +
            "ru Russian\n" +
            "sv, se Swedish\n" +
            "sk Slovak\n" +
            "sl Slovenian\n" +
            "sp, es Spanish\n" +
            "sr Serbian\n" +
            "th Thai\n" +
            "tr Turkish\n" +
            "ua, uk Ukrainian\n" +
            "vi Vietnamese\n" +
            "zh_cn Chinese Simplified\n" +
            "zh_tw Chinese Traditional\n" +
            "zu Zulu\n"
}

// First testing working library
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

