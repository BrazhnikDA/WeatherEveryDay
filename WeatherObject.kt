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

    // Размер форматирования на основе самоой длинной строки в переменной
    override fun toString(): String {
        val outString: StringBuilder = StringBuilder()

        val formattedStringTitle = "| %-10s"
        val formattedValueString = "%-18s |\n"
        val formattedValueDouble = "%-18.1f |\n"

        val cityTitleStr = "City:"
        val tempTitleStr = "Temp.:"
        val feelsTitleStr = "Feels:"
        val weatherTitleStr = "Weather:"
        val conditionTitleStr = "Status:"
        val windTitleStr = "Wind:"
        val dateTitleStr = "Date:"
        val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/y H:m:ss"))

        outString.append("- - - - - - - - - - - - - - - -\n")
        outString.append(formattedStringTitle.format(cityTitleStr) + formattedValueString.format(name))
        outString.append(formattedStringTitle.format(tempTitleStr) + formattedValueDouble.format(main))
        outString.append(formattedStringTitle.format(feelsTitleStr) + formattedValueDouble.format(feelsLike))
        outString.append(formattedStringTitle.format(weatherTitleStr) + formattedValueString.format(weather))
        outString.append(formattedStringTitle.format(conditionTitleStr) + formattedValueString.format(description))
        outString.append(formattedStringTitle.format(windTitleStr) + formattedValueDouble.format(speed))
        outString.append(formattedStringTitle.format(dateTitleStr) + formattedValueString.format(dateTime))
        outString.append("- - - - - - - - - - - - - - - -\n")

        return outString.toString()
    }
}