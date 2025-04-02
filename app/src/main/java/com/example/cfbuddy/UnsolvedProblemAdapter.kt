package com.example.cfbuddy

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cfbuddy.databinding.UnsolvedBinding

class ProblemAdapter : RecyclerView.Adapter<ProblemAdapter.ProblemViewHolder>() {
    
    inner class ProblemViewHolder (val binding: UnsolvedBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<Submission>() {
        override fun areItemsTheSame(oldItem: Submission, newItem: Submission): Boolean {
            return oldItem.id== newItem.id
        }
        override fun areContentsTheSame(oldItem: Submission, newItem: Submission): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    var submissions: List<Submission>
        get() =differ.currentList
        set(value) { differ.submitList(value) }

    override fun getItemCount(): Int {
        return submissions.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemViewHolder {
        return ProblemViewHolder(
            UnsolvedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProblemViewHolder, position: Int) {
        holder.binding.apply {
            val submission = submissions[position]
            tvProblemName.text = "${submission.problem.index}. ${submission.problem.name}"
            if (submission.problem.rating!=null) tvRating.text = "Rating: ${submission.problem.rating}" else "Rating: Not Available"
            tvVerdict.text = "Verdict: ${submission.verdict}"
        }
    }

}