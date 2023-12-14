package edu.uw.ischool.avajjh.spacemaniacs

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class EventsResult: AppCompatActivity() {
    private lateinit var recyclerViewEvent: RecyclerView
    private lateinit var adapter: Adapter
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.event_layout_2)

        recyclerViewEvent = findViewById(R.id.recyclerViewEvent)

        val eventList = (application as RepositoryApplication).repository.getEvents().map {
            "${it.name} - ${it.date}"
        }
        adapter = Adapter(this, eventList) { clickedItem ->

        }
    }


}