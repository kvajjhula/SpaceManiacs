package edu.uw.ischool.avajjh.spacemaniacs

import android.app.Application
import android.util.Log
import kotlinx.coroutines.runBlocking
import java.io.FileReader

class RepositoryApplication : Application() {
//    lateinit var repository: DataRepository

    override fun onCreate() {
        super.onCreate()
        Log.i("RepositoryApplication", "Created!")
    }

    fun update(fileName: String) {
        val dir = getExternalFilesDir("SpaceManiacs").toString() + "/" + fileName + ".json"
        Log.i("FileReader", "attempting to read")
        try {
            val reader = FileReader(dir)
            val text = reader.readText()
            reader.close()
            Log.i("FileReader", text)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}