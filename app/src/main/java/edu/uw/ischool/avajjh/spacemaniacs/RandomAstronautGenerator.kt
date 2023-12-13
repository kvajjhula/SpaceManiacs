package edu.uw.ischool.avajjh.spacemaniacs

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RandomAstronautGenerator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_astronaut_generator)
        val randomizeButton = findViewById<Button>(R.id.randomizeButton);
        randomizeButton.setOnClickListener() {
            val serviceIntent = Intent(this, FetchWrite::class.java).apply {
//                putExtra("params", "launch/upcoming/")
//                putExtra("fileName", "launches")

//                putExtra("params", "event/upcoming/")
//                putExtra("fileName", "events")

                putExtra("params", "astronaut")
                putExtra("fileName", "astronaut")
//                (application as RepositoryApplication).update("astronaut")
                (application as RepositoryApplication).update("launches")



            }
            this?.startService(serviceIntent)
        }
    }
}