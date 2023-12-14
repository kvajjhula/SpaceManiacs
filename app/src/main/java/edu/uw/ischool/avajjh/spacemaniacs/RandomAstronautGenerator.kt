package edu.uw.ischool.avajjh.spacemaniacs

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class RandomAstronautGenerator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_astronaut_generator)
        val randomizeButton = findViewById<Button>(R.id.randomizeButton)
        val astronautName = findViewById<TextView>(R.id.astronautName)
        val astronautAge = findViewById<TextView>(R.id.astronautAge)
        val astronautNationality = findViewById<TextView>(R.id.astronautNationality)
        val astronautFlight = findViewById<TextView>(R.id.astronautFlightCount)
        val astronautBio = findViewById<TextView>(R.id.astronautBio)
        var astronautImageURL = ""

        randomizeButton.setOnClickListener() {
            GlobalScope.launch(Dispatchers.Main) {
                fetchAndProcessData()
                Log.i("button", "clicked on randomize")
                (application as RepositoryApplication).update("astronaut")
                val astronautArray: Array<Astronaut> = (application as RepositoryApplication).repository.getAstronauts()
                val astronautArrayLength = astronautArray.size - 1 // should be 10?
                val randomAstronaut = astronautArray[(0..astronautArrayLength).random()]

                astronautName.text = "Name: ${randomAstronaut.name}"
                astronautAge.text = "Age:  ${randomAstronaut.age}"
                astronautNationality.text = "Nationality: ${randomAstronaut.nationality}"
                astronautFlight.text = "Flight Count: ${randomAstronaut.flightCount}"
                astronautBio.text = "Bio: ${randomAstronaut.bio}"
                astronautImageURL = randomAstronaut.profileImage

            }
        }
    }

    suspend fun fetchAndProcessData() {
        val fetchIntent = Intent(this, FetchWrite::class.java).apply {
            putExtra("params", "astronaut/")
            putExtra("fileName", "astronaut")
        }
        this?.startService(fetchIntent)
    }
}