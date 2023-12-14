package edu.uw.ischool.avajjh.spacemaniacs

import android.app.Application
import android.util.Log
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

interface FetchWriteCallback {
    fun onUpdateCompleted()
}
class RepositoryApplication : Application() {
    lateinit var repository: DataRepository

    override fun onCreate() {
        super.onCreate()
        repository = DataRepository()
        Log.i("RepositoryApplication", "Created!")
    }

    //Fetches from the API using fetchParams and writes to fileName in the external directory
    fun fetchWrite(callback: FetchWriteCallback, fetchParams: String, fileName: String) {
        val mainActivity = this
        val executor: Executor = Executors.newSingleThreadExecutor()
        Log.i("Download", "Before executor is running")

        executor.execute {
            val endpoint = "https://lldev.thespacedevs.com/2.2.0/" + fetchParams
            Log.i("Download", endpoint)
            val url = URL(endpoint)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            Log.i("Download", "inside executor")
            Log.i("Download", connection.responseCode.toString())
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.getInputStream()
                val reader = InputStreamReader(inputStream)
                Log.i("Download", "Downloading and writing JSON")
                val folder = getExternalFilesDir("SpaceManiacs")
                val file = File(folder, fileName)
                val outputStream = FileOutputStream(file)
                outputStream.write(reader.readText().toByteArray())
                reader.close()
                outputStream.close()
            }
            callback.onUpdateCompleted()
        }
    }

    //updates the data stored in the repository, reading from fileName in the external directory
    fun update(fileName: String) {
        val dir = getExternalFilesDir("SpaceManiacs").toString() + "/" + fileName + ".json"
        Log.i("FileReader", "attempting to read")
        try {
            val reader = FileReader(dir)
            val responseObject = JSONObject(reader.readText())
            reader.close()
            val responseArray = responseObject.getJSONArray("results")
            Log.i("length", responseArray.length().toString())
            if (fileName == "events") {
                val resultArray: Array<Event> = parseEvents(responseArray)
                repository.updateEvents(resultArray)
            } else if (fileName == "astronauts") {
                parseAstronauts(responseArray)
            } else if (fileName == "launches") {
                parseLaunches(responseArray)
            } else {
                Log.e("update", "Invalid Filename")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Helper function that parses the events.json string and returns an Array of Event
    fun parseEvents(responseArray: org.json.JSONArray): Array<Event> {
        Log.i("parseEvents", "parsing")
        var resultArray: Array<Event> = Array(responseArray.length()) {
            val eventsObject = responseArray.getJSONObject(it)
            val typeObject = eventsObject.getJSONObject("type")
            val name = eventsObject.getString("name")
            val description = eventsObject.getString("description")
            val location = eventsObject.getString("location")
            val featureImage =eventsObject.getString("feature_image")
            val date = eventsObject.getString("date")
            val type = typeObject.getString("name")
            Event(name, description, location, featureImage, date, type)
        }
        return resultArray
    }

    fun parseAstronauts(resultArray: org.json.JSONArray) {
        Log.i("parseAstronauts", "parsing")
    }
    fun parseLaunches(resultArray: org.json.JSONArray) {
        Log.i("parsingLaunches", "parsing")
    }
}