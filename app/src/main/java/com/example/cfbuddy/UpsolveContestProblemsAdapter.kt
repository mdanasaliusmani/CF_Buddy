package com.example.cfbuddy

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cfbuddy.UpsolveContestAdapter.UpsolveContestViewHolder
import com.example.cfbuddy.databinding.UpsolveContestsBinding
import com.example.cfbuddy.databinding.UpsolveProblemBinding

class UpsolveContestProblemsAdapter(private val context: Context): RecyclerView.Adapter<UpsolveContestProblemsAdapter.UpsolveContestProblemsViewHolder>() {

    inner class UpsolveContestProblemsViewHolder(val binding: UpsolveProblemBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<UpsolveProblemOutput>() {
        override fun areItemsTheSame(oldItem: UpsolveProblemOutput, newItem: UpsolveProblemOutput): Boolean {
            return oldItem.contestId == newItem.contestId
        }
        override fun areContentsTheSame(oldItem: UpsolveProblemOutput, newItem: UpsolveProblemOutput): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    var upsolve_contest_problems: MutableList<UpsolveProblemOutput>
        get() =differ.currentList
        set(value) { differ.submitList(value) }

    override fun getItemCount(): Int {
        return upsolve_contest_problems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpsolveContestProblemsViewHolder {
        return UpsolveContestProblemsViewHolder(
            UpsolveProblemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UpsolveContestProblemsViewHolder, position: Int) {
        holder.binding.apply {
            val upsolve_contest = upsolve_contest_problems[position]
            tvProblemName.text = upsolve_contest.name
            if (upsolve_contest.rating!=null) tvRating.text = "Rating: ${upsolve_contest.rating}" else "Rating: Not Available"
            tvStatus.text = "Status: ${upsolve_contest.status}"
            clUpsolveProblem.background = when {
                upsolve_contest.status=="Accepted" -> ContextCompat.getDrawable(context, R.color.light_green)
                upsolve_contest.status=="Not Attempted" -> ContextCompat.getDrawable(context, R.color.light_yellow)
                else -> ContextCompat.getDrawable(context, R.color.light_red)
            }
        }
    }
}