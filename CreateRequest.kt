import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.util.*

private const val API_KEY = "5ba6d4e9b64a6355a8c8222fda310aeb"
private const val URL_PART_FIRST = "https://api.openweathermap.org/data/2.5/weather?q="
private const val URL_PART_SECOND = "&units=metric&lang="
private const val URL_PART_THIRD = "&appid="

private const val KEY_NAME = "name"
private const val KEY_WEATHER = "weather"
private const val KEY_MAIN = "main"
private const val KEY_WIND = "wind"

private const val KEY_FEATURES_DESCRIPTION = "description"
private const val KEY_FEATURES_TEMP = "temp"
private const val KEY_FEATURES_FEELS = "feels_like"
private const val KEY_FEATURES_SPEED = "speed"

/**
 * Class for send requests
 */
class CreateRequest : ICreateRequest {
    var cityTitleStr = "City:"
    var tempTitleStr = "Temperature:"
    var feelsTitleStr = "Feels:"
    var conditionTitleStr = "Status:"
    var windTitleStr = "Wind:"
    var dateTitleStr = "Date:"

    /**
     * Send a request to get the weather
     *
     * @param city current city
     * @param language current language
     * @return response from the server in json format
     */
    override suspend fun requestToWeather(city: String, language: String): JsonObject {
        val client = HttpClient(CIO)
        val response: HttpResponse =
            client.get(URL_PART_FIRST + city.lowercase(Locale.getDefault()) + URL_PART_SECOND + language + URL_PART_THIRD + API_KEY)
        client.close()

        val jsonParser = JsonParser()
        return jsonParser.parse(java.lang.String.valueOf(io.ktor.utils.io.core.String(response.receive()))) as JsonObject
    }

    /**
     * Send a request for the existence of a city
     *
     * @param city current city
     * @return true - exist, false - not exist
     */
    override suspend fun requestToCheckExistCity(city: String): Boolean {
        val client = HttpClient(CIO)
        val response: HttpResponse =
            client.get("https://nominatim.openstreetmap.org/search.php?q=" + city.lowercase(Locale.getDefault()) + "&format=jsonv2&debug=1")

        client.close()

        val stringBody: String = response.receive()

        return stringBody.contains("Valid Tokens:")
    }

    /**
     * Request for text translation
     *
     * The service allows you to make no more than 15 requests per HOUR
     *
     * @param text for translate
     * @param prevLanguage start language
     * @param nextLanguage end language
     * @return translate text
     */
    override suspend fun requestTranslateText(text: String, prevLanguage: String, nextLanguage: String): String {
        val client = HttpClient(CIO)
        val response = client.post<HttpResponse>("https://fasttranslator.herokuapp.com/api/v1/text/to/text") {
            headers {
                parameter("source", text)
                parameter("lang", "$prevLanguage-$nextLanguage")
                parameter("as", "json")
            }
        }
        client.close()

        if (response.status.value !in 200..299) {
            return "Error"
        }

        val jsonParser = JsonParser()
        val request = jsonParser.parse(io.ktor.utils.io.core.String(response.receive())) as JsonObject
        val data = request.getAsJsonPrimitive("data")
        return data.toString().replace("\"", "")
    }

    /**
     * Analysis of the response from the server with the weather
     *
     * Retrieves the necessary values from the json object
     *
     * @param jObject json object
     * @return Collected WeatherObject
     */
    override fun parseData(jObject: JsonObject): WeatherObject {
        val jsonArrWeather = jObject.getAsJsonArray(KEY_WEATHER)
        val jsonArrMain = jObject.getAsJsonObject(KEY_MAIN)
        val jsonArrWind = jObject.getAsJsonObject(KEY_WIND)

        val name = jObject.getAsJsonPrimitive(KEY_NAME)
        val description = jsonArrWeather.get(0).asJsonObject[KEY_FEATURES_DESCRIPTION]
        val main = jsonArrMain.asJsonObject[KEY_FEATURES_TEMP]
        val feelsLike = jsonArrMain.asJsonObject[KEY_FEATURES_FEELS]
        val speed = jsonArrWind.asJsonObject[KEY_FEATURES_SPEED]

        return WeatherObject(
            name.toString().replace("\"", ""),
            main.asDouble,
            description.toString().replace("\"", "").substring(0, 1).uppercase() +
                    description.toString().replace("\"", "").substring(1).lowercase(),
            feelsLike.asDouble,
            speed.asDouble
        )
    }

    /**
     * Set city
     *
     * Replaces ' ' with '-' for a valid query
     *
     * @param city current city
     * @return Final string
     */
    override fun selectCity(city: String): String {
        val resCity = StringBuilder()

        for (ch in city.lowercase()) {
            if (ch == ' ')
                resCity.append('-')
            else
                resCity.append(ch)
        }

        return resCity.toString()
    }

    /**
     * Set language
     *
     * Checking the specified language for correct input
     * and renaming fields in the final table
     * If input incorrect, language set by default 'en'
     *
     * @param prevLanguage start language
     * @param nextLanguage end language
     * @return Final language
     */
    override suspend fun selectLanguage(prevLanguage: String, nextLanguage: String): String {
        if (nextLanguage.isNotEmpty())
            if (nextLanguage.length == 2) {
                renameField(prevLanguage.lowercase(), nextLanguage.lowercase())
                return nextLanguage.lowercase()
            }
        return "en"
    }

    /**
     * Translation of all fields from the final weather table
     *
     * @param prevLanguage start language
     * @param nextLanguage end language
     */
    private suspend fun renameField(prevLanguage: String, nextLanguage: String) {
        val allTitle = StringBuilder(
            cityTitleStr + " _ " +
                    tempTitleStr + " _ " +
                    feelsTitleStr + " _ " +
                    conditionTitleStr + " _ " +
                    windTitleStr + " _ " +
                    dateTitleStr + " _ "
        )
        val resTranslate = requestTranslateText(allTitle.toString(), prevLanguage, nextLanguage)
        val titleArray = resTranslate.split(" _ ").toTypedArray()
        cityTitleStr = titleArray[0]
        tempTitleStr = titleArray[1]
        feelsTitleStr = titleArray[2]
        conditionTitleStr = titleArray[3]
        windTitleStr = titleArray[4]
        dateTitleStr = titleArray[5]
    }
}