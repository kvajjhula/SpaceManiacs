package edu.uw.ischool.avajjh.spacemaniacs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity


class EventsLandingPage : AppCompatActivity() {
    lateinit var dropDownSpaceAgency: Spinner
    lateinit var dropDownYear: Spinner
    lateinit var dropDownEventType: Spinner
    lateinit var buttonSearch: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.events_layout_1)
        dropDownSpaceAgency = findViewById(R.id.spinnerSpaceAgency)
        dropDownYear = findViewById(R.id.spinnerYear)
        dropDownEventType = findViewById(R.id.spinnerYear)
        buttonSearch = findViewById(R.id.buttonSearch)

        buttonSearch.setOnClickListener {
//            val fetchIntent = Intent(this, FetchWrite::class.java).apply {
//                putExtra("params", "launch/upcoming/")
//                putExtra("fileName", "launches")
//            }
//            this?.startService(fetchIntent)
            Log.i("button", "clicked on search")
            (application as RepositoryApplication).update("launches")
        }
    }
}