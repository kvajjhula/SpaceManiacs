package edu.uw.ischool.avajjh.spacemaniacs

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RandomAstronautGenerator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_astronaut_generator)
        val randomizeButton = findViewById<Button>(R.id.randomizeButton)
        val favoritesButton = findViewById<Button>(R.id.favoritesButton)

        favoritesButton.setOnClickListener() {
            
        }

        randomizeButton.setOnClickListener() {
            Log.i("button", "clicked on randomize")
            (application as RepositoryApplication).fetchWrite(fetchCallBack, "astronaut/", "astronaut.json")
            (application as RepositoryApplication).update("astronaut")
        }
    }

    //excutes the following functions after the fetch write function completes
    val fetchCallBack = object:FetchWriteCallback {
        override fun onUpdateCompleted() {
            Log.i("insideCallback", "hi")
            (application as RepositoryApplication).update("astronaut")
            val astronautArray: Array<Astronaut> = (application as RepositoryApplication).repository.getAstronauts()
            Log.i("Data", astronautArray[0].name)

            runOnUiThread() {
            val astronautName = findViewById<TextView>(R.id.astronautName)
            val astronautAge = findViewById<TextView>(R.id.astronautAge)
            val astronautNationality = findViewById<TextView>(R.id.astronautNationality)
            val astronautFlight = findViewById<TextView>(R.id.astronautFlightCount)
            val astronautBio = findViewById<TextView>(R.id.astronautBio)
            val astronautImage = findViewById<ImageView>(R.id.astronautImage)


            val astronautArrayLength = astronautArray.size - 1 // should be 10?
            val randomAstronaut = astronautArray[(0..astronautArrayLength).random()]
            val astronautImageURL = randomAstronaut.profileImage

            astronautName.text = "Name: ${randomAstronaut.name}"
            astronautAge.text = "Age:  ${randomAstronaut.age}"
            astronautNationality.text = "Nationality: ${randomAstronaut.nationality}"
            astronautFlight.text = "Flight Count: ${randomAstronaut.flightCount}"
            astronautBio.text = "Bio: ${randomAstronaut.bio}"
            Picasso.get()
                .load(astronautImageURL)
                .into(astronautImage)
            }
        }
    }



}