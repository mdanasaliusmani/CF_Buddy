package com.example.cfbuddy

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cfbuddy.databinding.UpsolveContestsBinding

class UpsolveContestAdapter(private val context: Context): RecyclerView.Adapter<UpsolveContestAdapter.UpsolveContestViewHolder>() {

    inner class UpsolveContestViewHolder(val binding: UpsolveContestsBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<UpsolveContest>() {
        override fun areItemsTheSame(oldItem: UpsolveContest, newItem: UpsolveContest): Boolean {
            return oldItem.contestId == newItem.contestId
        }
        override fun areContentsTheSame(oldItem: UpsolveContest, newItem: UpsolveContest): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    var upsolve_contests: List<UpsolveContest>
        get() =differ.currentList
        set(value) { differ.submitList(value) }

    override fun getItemCount(): Int {
        return upsolve_contests.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpsolveContestViewHolder {
        return UpsolveContestViewHolder(
            UpsolveContestsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UpsolveContestViewHolder, position: Int) {
        holder.binding.apply {
            val upsolve_contest = upsolve_contests[position]
            tvContestName.text = upsolve_contest.contestName
            tvRank.text = "Rank: ${upsolve_contest.rank}"
            tvOldRating.text = "Old Rating: ${upsolve_contest.oldRating}"
            tvNewRating.text = "New Rating: ${upsolve_contest.newRating}"
            clUpsolveContest.background = when {
                upsolve_contest.newRating>upsolve_contest.oldRating -> ContextCompat.getDrawable(context, R.color.light_green)
                upsolve_contest.newRating<upsolve_contest.oldRating -> ContextCompat.getDrawable(context, R.color.light_red)
                else -> ContextCompat.getDrawable(context, R.color.light_yellow)
            }
            cvUpsolveContest.setOnClickListener {
                val intent = Intent(context, UpsolveContestProblemsActivity::class.java)
                intent.putExtra("contestId", upsolve_contest.contestId)
                startActivity(context, intent, null)
            }
            Log.e("BG_Color", clUpsolveContest.background.toString())
        }
    }
}