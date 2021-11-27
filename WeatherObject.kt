import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherObject(
    name_: String,
    weather_: String,
    main_: Double,
    description_: String,
    feelsLike_: Double,
    speed_: Double
) {
    private var name: String = name_
    private var weather: String = weather_
    private var main: Double = main_
    private var description: String = description_
    private var feelsLike: Double = feelsLike_
    private var speed: Double = speed_

    init {
        name = name_
        weather = weather_
        main = main_
        description = description_
        feelsLike = feelsLike_
        speed = speed_
    }

    class TranslateRequest(weatherObject: WeatherObject) {
        val weatherObject = weatherObject
        val cityTitleStr = "City:"
        val tempTitleStr = "Temp.:"
        val feelsTitleStr = "Feels:"
        val weatherTitleStr = "Weather:"
        val conditionTitleStr = "Status:"
        val windTitleStr = "Wind:"
        val dateTitleStr = "Date:"

        suspend fun translate(): String {
            val strOut = StringBuilder()
            strOut.append(requestTranslateText(weatherObject.toString(), "ru"))
            return strOut.toString()
        }

        private suspend fun requestTranslateText(text: String,language: String): String {
            val client = HttpClient(CIO)
            val response = client.post<HttpResponse>("https://fasttranslator.herokuapp.com/api/v1/text/to/text") {
                headers {
                    parameter("source", "$text")
                    parameter("lang", "en-$language")
                    parameter("as", "json")
                }
            }
            client.close()

            val jsonParser = JsonParser()
            val request = jsonParser.parse(io.ktor.utils.io.core.String(response.receive())) as JsonObject
            val data = request.getAsJsonPrimitive("data")
            return data.toString().replace("\"", "")
        }
    }

    suspend fun getA() {
        val a = TranslateRequest(this)
        a.translate()
    }

    override fun toString(): String {
        val sizeTitle = "10"
        val size = getIsLargeString()
        val outString: StringBuilder = StringBuilder()

        val formattedStringTitle = "| %-${sizeTitle}s"
        val formattedValueString = "%-${size}s |\n"
        val formattedValueDouble = "%-${size}.1f |\n"

        val cityTitleStr = "City:"
        val tempTitleStr = "Temp.:"
        val feelsTitleStr = "Feels:"
        val weatherTitleStr = "Weather:"
        val conditionTitleStr = "Status:"
        val windTitleStr = "Wind:"
        val dateTitleStr = "Date:"
        val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/y H:m:ss"))

        outString.append("${getSeparationLine(sizeTitle.toInt() + size.toInt() + 3)}\n")
        outString.append(formattedStringTitle.format(cityTitleStr) + formattedValueString.format(name))
        outString.append(formattedStringTitle.format(tempTitleStr) + formattedValueDouble.format(main))
        outString.append(formattedStringTitle.format(feelsTitleStr) + formattedValueDouble.format(feelsLike))
        outString.append(formattedStringTitle.format(weatherTitleStr) + formattedValueString.format(weather))
        outString.append(formattedStringTitle.format(conditionTitleStr) + formattedValueString.format(description))
        outString.append(formattedStringTitle.format(windTitleStr) + formattedValueDouble.format(speed))
        outString.append(formattedStringTitle.format(dateTitleStr) + formattedValueString.format(dateTime))
        outString.append("${getSeparationLine(sizeTitle.toInt() + size.toInt() + 3)}\n")

        return outString.toString()
    }

    /**
     *
     * @return size
     */
    private fun getIsLargeString(): String {
        val listString: List<String> =
            mutableListOf(name, weather, main.toString(), description, feelsLike.toString(), speed.toString(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/y H:m:ss")))
        var maxSize = 0

        for (item in listString) {
            if (item.length > maxSize)
                maxSize = item.length
        }
        return maxSize.toString()
    }

    /**
     * @param size count character in table in width
     * @return string into separation line '- '
     */
    private fun getSeparationLine(size: Int): String {
        val sepLine = StringBuilder("")
        for(i in 0 until size / 2 + 1) {
            sepLine.append("- ")
        }
        return sepLine.toString()
    }
}