package com.example.cfbuddy

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cfbuddy.databinding.UpcomingContestBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ContestListAdapter: RecyclerView.Adapter<ContestListAdapter.ContestViewHolder>() {

    inner class ContestViewHolder(val binding: UpcomingContestBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<UpcomingContest>() {
        override fun areItemsTheSame(oldItem: UpcomingContest, newItem: UpcomingContest): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: UpcomingContest, newItem: UpcomingContest): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    var contests: List<UpcomingContest>
        get() =differ.currentList
        set(value) { differ.submitList(value) }

    override fun getItemCount(): Int {
        return contests.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContestViewHolder {
        return ContestViewHolder(UpcomingContestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ContestViewHolder, position: Int) {
        holder.binding.apply {
            val contest = contests[position]
            val days = if (contest.durationSeconds.toInt() / 86400 != 0) "${contest.durationSeconds.toInt() / 86400} days" else ""
            val hours = if (contest.durationSeconds.toInt() % 86400 / 3600 != 0) "${contest.durationSeconds.toInt() % 86400 / 3600} hr" else ""
            val minutes = if (contest.durationSeconds.toInt() % 3600 / 60 != 0) "${contest.durationSeconds.toInt() % 3600 / 60} min" else ""
            val date = Date(contest.startTimeSeconds * 1000)
            val contestDateTimeFormatter = SimpleDateFormat("E, dd/MM/yyyy | h:mm a", Locale.getDefault())
            val contestDateTimeString = contestDateTimeFormatter.format(date)
            tvContestName.text = contest.name
            tvContestType.text = "Type: ${contest.type}"
            tvContestDuration.text = "Duration: $days $hours $minutes".trim() // trim to remove extra spaces if some fields are empty
            tvContestStartTime.text = "Starts: $contestDateTimeString"
        }
    }
}