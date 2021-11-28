/**
 * Class for storage user settings
 *
 * @param city_ Current city
 * @param language_ Current language
 */
class UserSettings(city_: String, language_: String) {
    var city = city_
    var language = language_

    //fun getCity(): String = city.ifEmpty { "" }
    //fun getLanguage(): String = language.ifEmpty { "" }

    init {
        city = city_
        language = language_
    }

    /**
     * Output to console current city and language
     */
    override fun toString(): String {
        return "City: $city\n Lang-ge: $language"
    }
}