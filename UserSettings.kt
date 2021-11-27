class UserSettings(city_: String, language_: String) {
    private var city = city_
    private var language = language_

    fun getCity(): String = city.ifEmpty { "" }
    fun getLanguage(): String = language.ifEmpty { "" }

    init {
        city = city_
        language = language_
    }

    override fun toString(): String {
        return "City: ${getCity()}\n Lang-ge: ${getLanguage()}"
    }
}