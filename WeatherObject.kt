import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Class for storage object weather
 *
 * @param name_ Name city
 * @param main_ Current temperature
 * @param description_ Description of the weather
 * @param feelsLike_ Temperature sensations
 * @param speed_ Wind speed
 */
class WeatherObject(
    name_: String,
    main_: Double,
    description_: String,
    feelsLike_: Double,
    speed_: Double
) {
    private var name: String = name_
    private var main: Double = main_
    private var description: String = description_
    private var feelsLike: Double = feelsLike_
    private var speed: Double = speed_

    init {
        name = name_
        main = main_
        description = description_
        feelsLike = feelsLike_
        speed = speed_
    }

    /**
     * Output of the weather table to the console
     *
     * @param createRequest Object for creating queries
     * @return final table
     */
    fun outToConsole(createRequest: CreateRequest): String {
        val sizeTitle = "13"
        val size = getIsLargeString()
        val outString: StringBuilder = StringBuilder()

        val formattedStringTitle = "| %-${sizeTitle}s"
        val formattedValueString = "%-${size}s |\n"
        val formattedValueDouble = "%-${size}.1f |\n"

        val cityTitleStr = createRequest.cityTitleStr
        val tempTitleStr = createRequest.tempTitleStr
        val feelsTitleStr = createRequest.feelsTitleStr
        val conditionTitleStr = createRequest.conditionTitleStr
        val windTitleStr = createRequest.windTitleStr
        val dateTitleStr = createRequest.dateTitleStr
        val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/y H:m:ss"))

        outString.append("${getSeparationLine(sizeTitle.toInt() + size.toInt() + 3)}\n")
        outString.append(formattedStringTitle.format(cityTitleStr) + formattedValueString.format(name))
        outString.append(formattedStringTitle.format(tempTitleStr) + formattedValueDouble.format(main))
        outString.append(formattedStringTitle.format(feelsTitleStr) + formattedValueDouble.format(feelsLike))
        outString.append(formattedStringTitle.format(conditionTitleStr) + formattedValueString.format(description))
        outString.append(formattedStringTitle.format(windTitleStr) + formattedValueDouble.format(speed))
        outString.append(formattedStringTitle.format(dateTitleStr) + formattedValueString.format(dateTime))
        outString.append("${getSeparationLine(sizeTitle.toInt() + size.toInt() + 3)}\n")

        return outString.toString()
    }

    /**
     * Search for the longest string
     *
     * @return size
     */
    private fun getIsLargeString(): String {
        val listString: List<String> =
            mutableListOf(
                name, main.toString(), description, feelsLike.toString(), speed.toString(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/y H:m:ss"))
            )
        var maxSize = 0

        for (item in listString) {
            if (item.length > maxSize)
                maxSize = item.length
        }
        return maxSize.toString()
    }

    /**
     * Drawing the dividing line relative to the size of the table
     *
     * @param size count character in table in width
     * @return string into separation line '- '
     */
    private fun getSeparationLine(size: Int): String {
        val sepLine = StringBuilder("")
        for (i in 0 until size / 2 + 1) {
            sepLine.append("- ")
        }
        return sepLine.toString()
    }
}