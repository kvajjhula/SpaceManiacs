package edu.uw.ischool.avajjh.spacemaniacs

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Locale

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
                holder.bindImage(it.image)
                holder.bindCountdown(it.windowStart, it.windowEnd)
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
        val headerWindowStartTextView: TextView = itemView.findViewById(R.id.upcomingLaunchCountdown)
        val upcomingImage: ImageView = itemView.findViewById(R.id.upcomingLaunchImage)
        val notifyButton: Button = itemView.findViewById(R.id.notifyButton)
        private var countDownTimer: CountDownTimer? = null


        fun bindImage(url: String) {
            Picasso.get().load(url).into(upcomingImage)
        }

        fun bindCountdown(windowStart: String, windowEnd: String) {
            val timeDifference = calculateTimeDifference(windowStart, windowEnd)

            val countdownTextView: TextView = itemView.findViewById(R.id.upcomingLaunchCountdown)
            val countdownLabel: TextView = itemView.findViewById(R.id.upcomingLaunchCountdownLabel)

            countdownLabel.text = "Countdown to Launch:"

            object : CountDownTimer(timeDifference, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val hours = millisUntilFinished / (1000 * 60 * 60)
                    val minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60)
                    val seconds = (millisUntilFinished % (1000 * 60)) / 1000

                    val countdownText = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    countdownTextView.text = countdownText
                }

                override fun onFinish() {
                    Log.d("Countdown", "Countdown finished!")
                }
            }.start()
        }


        fun calculateTimeDifference(windowStart: String, windowEnd: String): Long {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")

            try {
                val startDate = dateFormat.parse(windowStart)
                val endDate = dateFormat.parse(windowEnd)

                if (startDate != null && endDate != null) {
                    return endDate.time - startDate.time
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return 0
        }

        private fun formatMillisToTime(millis: Long): String {
            val hours = millis / (1000 * 60 * 60)
            val minutes = (millis % (1000 * 60 * 60)) / (1000 * 60)
            val seconds = (millis % (1000 * 60)) / 1000
            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }

        // Add a method to cancel the countdown when needed
        fun cancelCountdown() {
            countDownTimer?.cancel()
        }
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
