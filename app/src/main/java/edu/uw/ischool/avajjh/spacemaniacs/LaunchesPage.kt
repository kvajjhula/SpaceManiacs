package edu.uw.ischool.avajjh.spacemaniacs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LaunchesPage : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshButton: Button
//    private lateinit var launchAdapter: SimpleLaunchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launches_page)

//        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)

        refreshButton = findViewById(R.id.refresh)


        refreshButton.setOnClickListener {
            GlobalScope.launch {
                fetchAndProcessData()
                Log.i("button", "clicked on refresh")
                (application as RepositoryApplication).update("launches")
                val launchArray: Array<Launch> = (application as RepositoryApplication).repository.getLaunches()
                Log.i("DataLaunch", launchArray[0].name)
                Log.i("DataLaunch", launchArray[1].name)
                Log.i("DataLaunch", launchArray[1].windowStart)
            }
        }

        // Initial setup of the adapter and RecyclerView
//        val launchesArray = repository.getLaunches()
//        launchAdapter = SimpleLaunchAdapter(launchesArray.toList())
//        recyclerView.adapter = launchAdapter
    }


    suspend fun fetchAndProcessData() {
        val fetchIntent = Intent(this, FetchWrite::class.java).apply {
            putExtra("params", "launch/upcoming/")
            putExtra("fileName", "launches")
        }
        this?.startService(fetchIntent)
    }
}

//class SimpleLaunchAdapter(private var launchList: List<Launch>) :
//    RecyclerView.Adapter<SimpleLaunchAdapter.ViewHolder>() {
//
//    // Function to update the data in the adapter
//    fun updateData(newLaunchList: List<Launch>) {
//        launchList = newLaunchList
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val launch = launchList[position]
//        holder.titleTextView.text = launch.name
//        holder.descriptionTextView.text = launch.description
//    }
//
//    override fun getItemCount(): Int {
//        return launchList.size
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
//        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
//    }
//}
