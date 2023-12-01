package edu.uw.ischool.avajjh.spacemaniacs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    lateinit var cardViewLaunches: CardView
    lateinit var cardViewAstronauts: CardView
    lateinit var cardViewEvents: CardView
    lateinit var cardViewFavorites: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardViewLaunches = findViewById(R.id.cardViewLaunches)
        cardViewAstronauts = findViewById(R.id.cardViewAstronauts)
        cardViewEvents = findViewById(R.id.cardViewEvents)
        cardViewFavorites = findViewById(R.id.cardViewFavorites)

        cardViewLaunches.setOnClickListener {
            Log.i("Main", "clicked launches")
        }

        cardViewAstronauts.setOnClickListener {
            Log.i("Main", "clicked astronauts")
        }

        cardViewEvents.setOnClickListener {
            Log.i("Main", "clicked events")

        }

        cardViewFavorites.setOnClickListener {
            Log.i("Main", "clicked favorites")

        }


    }
}