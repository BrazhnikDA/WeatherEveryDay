import kotlin.system.exitProcess

/**
 * Main function for start application
 */
suspend fun main() {
    menu()
}

/**
 * Set current city
 *
 * Ask the user the city and check if it exists,
 * if not, ask them to enter it again
 *
 * @param request object for working with queries
 * @param inLanguage select language
 * @return current city
 */
suspend fun inputCity(request: CreateRequest, inLanguage: String): String {
    var inCity: String
    do {
        println("Please select city: ")
        inCity = readLine().toString()
        val isExist = request.requestToCheckExistCity(request.selectCity(inLanguage))
        if (!isExist)
            println("City not found. Try again!")
    } while (!isExist)
    return inCity
}

/**
 * Set current language
 *
 * Ask the user the language and check if it exists,
 * if not, ask them to set en by default
 *
 * If input '1', print guid info by languages
 *
 * @return current language
 */
fun inputLanguage(): String {
    var inLanguage: String
    do {
        println(
            "Input \"1\" for get guid info\n" +
                    "Please select language: "
        )
        inLanguage = readLine().toString()
        if (inLanguage == "1")
            println(viewGuid())
    } while (inLanguage == "1")
    return inLanguage
}

/**
 * Calling the main menu
 *
 * The main menu for user interaction contains 4 items.
 * 1. Display the weather 2. Change the city.
 * 3 Change the language. 4 Exit the application
 *
 */
suspend fun menu() {
    val request = CreateRequest()

    var inLanguage = inputLanguage()
    var inCity = inputCity(request, inLanguage)

    val user = UserSettings(request.selectCity(inCity), request.selectLanguage("en", inLanguage))

    println(
        request.parseData(request.requestToWeather(user.city, user.language)).outToConsole(request) +
                "Select language: " + user.language
    )
    while (true) {
        println("Menu: ")
        println("1. View")
        println("2. Change city")
        println("3. Change language")
        print("0. Exit this application\n> ")

        when (readLine()) {
            "1" -> println(view(request, user))
            "2" -> {
                inCity = inputCity(request, inLanguage)
                user.city = request.selectCity(inCity)
                println(view(request, user))
            }
            "3" -> {
                val prevLanguage = user.language
                inLanguage = inputLanguage()
                user.language = request.selectLanguage(prevLanguage, inLanguage)
                println(view(request, user))
            }
            "0" -> exitProcess(0)
        }
    }
}

/**
 * Print the weather table
 *
 * @param request object for working with queries
 * @param user object for storage user data (city, lang.)
 * @return output
 */
private suspend fun view(request: CreateRequest, user: UserSettings): String {
    return request.parseData(request.requestToWeather(user.city, user.language)).outToConsole(request) +
            "Select language: " + user.language
}

/**
 * Get guid info for language codes
 *
 * @return list language code
 */
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
            "gl Galicia\n" +
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
            "pt_br Portugal Brazil\n" +
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

