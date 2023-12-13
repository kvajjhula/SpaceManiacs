package edu.uw.ischool.avajjh.spacemaniacs

import android.app.Application
import android.util.Log
import org.json.JSONObject
import java.io.FileReader

class RepositoryApplication : Application() {
    lateinit var repository: DataRepository

    override fun onCreate() {
        super.onCreate()
        repository = DataRepository()
        update("events")
        update("astronaut")
        update("launches")
        Log.i("RepositoryApplication", "Created!")
    }

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
                val resultArray: Array<Launch> = parseLaunches(responseArray)
                repository.updateLaunches(resultArray)
            } else {
                Log.e("update", "Invalid Filename")
            }
            Log.i("FileReader", responseArray.getJSONObject(0).getString("name"))

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

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
//            Log.i("index", it.toString())
//            Log.i("name", eventsObject.getString("name"))
//            Log.i("description", eventsObject.getString("description"))
//            Log.i("location", eventsObject.getString("location"))
//            Log.i("feature_image", eventsObject.getString("feature_image"))
//            Log.i("date", eventsObject.getString("date")::class.java.typeName)
//            Log.i("type", type)
            Event(name, description, location, featureImage, date, type)
        }
        return resultArray
    }

    fun parseAstronauts(resultArray: org.json.JSONArray) {
        Log.i("parseAstronauts", "parsing")
    }

    fun parseLaunches(responseArray: org.json.JSONArray) : Array<Launch> {
        Log.i("parsingLaunches", "parsing")
        var resultArray: Array<Launch> = Array(responseArray.length()) {
            val launchObjects = responseArray.getJSONObject(it)
            val missionObject = launchObjects.getJSONObject("mission")
            val name = launchObjects.getString("name")
            val windowStart = launchObjects.getString("window_start")
            val description = missionObject.getString("description")
            val image = launchObjects.getString("image")
//            Log.i("index", it.toString())
//            Log.i("name", name)
//            Log.i("window", windowStart)
//            Log.i("descr", description)
//            Log.i("image", image)
            Launch(name, windowStart, description, image)
        }
        return resultArray
    }

//    fun parseLaunches(responseArray: org.json.JSONArray): Array<Launch> {
//        Log.i("parsingLaunches", "parsing")
//        return Array(responseArray.length()) {
//            val launchObjects = responseArray.getJSONObject(it)
//            val missionObject = launchObjects.getJSONObject("mission")
//            val name = launchObjects.getString("name")
//            val windowStart = launchObjects.getString("window_start")
//            val description = missionObject.getString("description")
//            val image = launchObjects.getString("image")
//            Launch(name, windowStart, description, image)
//        }
//    }
}