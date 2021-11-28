/*
// DON'T WORK CORRECTLY
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class TranslateText {

    suspend fun languageDetection(text: String): String {
        val client = HttpClient(CIO)
        val response = client.get<HttpResponse>("https://fasttranslator.herokuapp.com/api/v1/detect") {
            headers {
                parameter("source", "$text")
            }
        }
        client.close()

        val jsonParser = JsonParser()
        val request = jsonParser.parse(io.ktor.utils.io.core.String(response.receive())) as JsonObject
        val data = request.getAsJsonPrimitive("Iso6391")
        return data.toString().replace("\"", "")
    }

}*/
