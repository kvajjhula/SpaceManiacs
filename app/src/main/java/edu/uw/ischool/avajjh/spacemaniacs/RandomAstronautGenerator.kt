package edu.uw.ischool.avajjh.spacemaniacs

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RandomAstronautGenerator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_astronaut_generator)
        val randomizeButton = findViewById<Button>(R.id.randomizeButton);
        randomizeButton.setOnClickListener() {
            GlobalScope.launch {
                fetchAndProcessData()
                Log.i("button", "clicked on randomize")
                (application as RepositoryApplication).update("astronaut")
                val astronautArray: Array<Astronaut> = (application as RepositoryApplication).repository.getAstronauts()
                Log.i("Data", astronautArray[0].name)
                Log.i("Data", astronautArray[1].name)
                Log.i("Data", astronautArray[2].name)
            }
        }
    }

    suspend fun fetchAndProcessData() {
        val fetchIntent = Intent(this, FetchWrite::class.java).apply {
            putExtra("params", "astronaut")
            putExtra("fileName", "astronaut")
        }
        this?.startService(fetchIntent)
    }
}