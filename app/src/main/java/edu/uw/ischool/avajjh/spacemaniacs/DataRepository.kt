package edu.uw.ischool.avajjh.spacemaniacs

import android.content.ContentValues.TAG
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

// Data class representing a launch with serialized names for Gson parsing
data class Launch(
    val name: String,
    val window_start: String,
    val window_end: String
)


// Data class representing the response with a list of launches
data class Response(
    @SerializedName("results")
    var launchList: List<Launch>? = null
)

// Interface for the data repository
interface DataRepoInterface {
    fun parseJson(response: String): Response?

    class DataRepository() : DataRepoInterface {

        // Custom deserializer to handle possible JSON variations
        class LaunchDeserializer : JsonDeserializer<Response> {
            override fun deserialize(
                json: JsonElement?,
                typeOfT: Type?,
                context: JsonDeserializationContext?
            ): Response {
                val jsonObject = json?.asJsonObject

                // Handle variations in JSON structure
                return if (jsonObject != null && jsonObject.has("results")) {
                    val gson = GsonBuilder().create()

                    // Print the raw JSON for debugging purposes
                    Log.d(TAG, "Raw JSON: $json")

                    // Print information about the parsing process
                    Log.d(TAG, "Parsing JSON with results field")

                    val parsedResponse = gson.fromJson(jsonObject, Response::class.java)

                    // Print the parsed response for debugging purposes
                    Log.d(TAG, "Parsed Response: $parsedResponse")

                    parsedResponse
                } else {
                    // Handle alternative JSON structure or error cases

                    // Print information about the parsing process
                    Log.d(TAG, "Parsing JSON without results field")

                    Response()
                }
            }
        }



        override fun parseJson(response: String): Response? {
            val gson = GsonBuilder()
                .registerTypeAdapter(Response::class.java, LaunchDeserializer())
                .create()

            return try {
                gson.fromJson(response, Response::class.java)
            } catch (e: JsonParseException) {
                e.printStackTrace()
                null
            }
        }
    }
}

