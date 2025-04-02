package com.example.cfbuddy

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cfbuddy.databinding.FragmentUnsolvedBinding
import com.example.cfbuddy.databinding.UnsolvedBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class UnsolvedFragment : Fragment() {

    lateinit var binding: FragmentUnsolvedBinding
    private lateinit var ProblemAdapter: ProblemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUnsolvedBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupRecyclerView() = binding.rvUnsolved.apply {
        ProblemAdapter = ProblemAdapter()
        adapter = ProblemAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding = FragmentUnsolvedBinding.inflate(layoutInflater)
        val handle = requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE).getString("user_handle", null)?: "Md_Anas_Ali_Usmani"

        lifecycleScope.launch {
            val response = try {
                RetrofitInstance.api.getUserStatus(handle)
            } catch (e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                return@launch
            }
            if (response.isSuccessful && response.body() != null && response.body()!!.status == "OK") {
                val submissions = response.body()!!.result
                val groupedSubmissions = submissions.groupBy { it.problem.contestId to it.problem.index }
                val unsolvedGroups = groupedSubmissions.filter { group -> group.value.none { it.verdict == "OK" } }
                val unsolvedProblems = unsolvedGroups.values.mapNotNull { it.firstOrNull() }
                ProblemAdapter.submissions = unsolvedProblems
            } else {
                Log.e(TAG, "Response not successful")
            }
            Log.e(TAG, response.body().toString())
        }
    }

}