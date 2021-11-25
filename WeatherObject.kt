import java.time.LocalDateTime

class WeatherObject(
    name_: String,
    weather_: String,
    main_: String,
    description_: String,
    feelsLike_: String,
    speed_: String
) {
    private var name: String = name_
    private var weather: String = weather_
    private var main: String = main_
    private var description: String = description_
    private var feelsLike: String = feelsLike_
    private var speed: String = speed_

    init {
        name = name_
        weather = weather_
        main = main_
        description = description_
        feelsLike = feelsLike_
        speed = speed_
    }

    override fun toString(): String {
        
        return "City:$name\n" +
                "Temperature:\t$main°\n" +
                "Feels like:\t$feelsLike°\n" +
                "Weather:\t$weather\n" +
                "Description:\t$description\n" +
                "Wind speed:\t$speed\n" +
                "Date: ${LocalDateTime.now()}\n"
    }
}