import com.google.gson.JsonObject


/**
 * Interface for send requests
 */
interface ICreateRequest {
    suspend fun requestToWeather(city: String, language: String): JsonObject
    suspend fun requestToCheckExistCity(city: String): Boolean
    suspend fun requestTranslateText(text: String, prevLanguage: String, nextLanguage: String): String

    fun parseData(jObject: JsonObject): WeatherObject

    fun selectCity(city: String): String
    suspend fun selectLanguage(prevLanguage: String, nextLanguage: String): String
}