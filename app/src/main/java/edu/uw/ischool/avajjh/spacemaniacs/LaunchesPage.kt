package edu.uw.ischool.avajjh.spacemaniacs

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LaunchesPage : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshButton: Button
    private lateinit var launchAdapter: SimpleLaunchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launches_page)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
//
        refreshButton = findViewById(R.id.refresh)
//
        launchAdapter = SimpleLaunchAdapter(emptyList())
        recyclerView.adapter = launchAdapter

        GlobalScope.launch(Dispatchers.Main) {
            fetchAndProcessData()
            Log.i("button", "clicked on refresh")
            (application as RepositoryApplication).update("launches")
            val launchArray: Array<Launch> = (application as RepositoryApplication).repository.getLaunches()
            launchAdapter.updateData(launchArray.toList())
        }


        refreshButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                fetchAndProcessData()
                Log.i("button", "clicked on refresh")
                (application as RepositoryApplication).update("launches")
                val launchArray: Array<Launch> = (application as RepositoryApplication).repository.getLaunches()
                launchAdapter.updateData(launchArray.toList())
//                Log.i("DataLaunch", launchArray[0].name)
//                Log.i("DataLaunch", launchArray[1].name)
//                Log.i("DataLaunch", launchArray[1].windowStart)
            }
        }
    }

    suspend fun fetchAndProcessData() {
        val fetchIntent = Intent(this, FetchWrite::class.java).apply {
            putExtra("params", "launch/upcoming/")
            putExtra("fileName", "launches")
        }
        this?.startService(fetchIntent)
    }
}

class SimpleLaunchAdapter(private var launchList: List<Launch>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    fun updateData(newLaunchList: List<Launch>) {
        launchList = newLaunchList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_HEADER else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            HeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.header_layout,
                    parent,
                    false
                )
            )
        } else {
            ItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_layout,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val launch = launchList[position - 1]
            holder.titleTextView.text = launch.name
            holder.descriptionTextView.text = launch.description
        } else if (holder is HeaderViewHolder && position == 0) {
            val upcomingLaunch = launchList.minByOrNull { it.windowStart }
            upcomingLaunch?.let {
                holder.headerTitleTextView.text = it.name
                holder.headerWindowStartTextView.text = it.windowStart
            }
        }
    }


    override fun getItemCount(): Int {
        return launchList.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerTitleTextView: TextView = itemView.findViewById(R.id.upcomingLaunchName)
        val headerWindowStartTextView: TextView = itemView.findViewById(R.id.upcomingLaunchWindowStart)
        val upcomingImage: ImageView = itemView.findViewById(R.id.upcomingLaunchImage)
        val notifyButton: Button = itemView.findViewById(R.id.notifyButton)
    }
}



//class SimpleLaunchAdapter(private var launchList: List<Launch>) :
//    RecyclerView.Adapter<SimpleLaunchAdapter.ViewHolder>() {
//
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
