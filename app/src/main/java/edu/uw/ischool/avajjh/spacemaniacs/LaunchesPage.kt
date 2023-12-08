package edu.uw.ischool.avajjh.spacemaniacs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class LaunchItem(val title: String, val description: String)


class LaunchesPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launches_page)

        val launchItemList = listOf(
            LaunchItem("Launch 1", "Description 1"),
            LaunchItem("Launch 2", "Description 2")
        )

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val launchAdapter = SimpleLaunchAdapter(launchItemList)
        recyclerView.adapter = launchAdapter
    }
}

class SimpleLaunchAdapter(private val launchItemList: List<LaunchItem>) :
    RecyclerView.Adapter<SimpleLaunchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launchItem = launchItemList[position]
        holder.titleTextView.text = launchItem.title
        holder.descriptionTextView.text = launchItem.description
    }

    override fun getItemCount(): Int {
        return launchItemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
    }
}