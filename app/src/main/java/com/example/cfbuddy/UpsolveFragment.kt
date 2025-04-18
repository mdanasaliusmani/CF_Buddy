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
import com.example.cfbuddy.databinding.FragmentCalendarBinding
import com.example.cfbuddy.databinding.FragmentUpsolveBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class UpsolveFragment : Fragment() {

    lateinit var binding: FragmentUpsolveBinding
    private lateinit var UpsolveContestAdapter: UpsolveContestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpsolveBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupRecyclerView() = binding.rvUpsolve.apply {
        UpsolveContestAdapter = UpsolveContestAdapter(requireContext())
        adapter = UpsolveContestAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding = FragmentUpsolveBinding.inflate(layoutInflater)
        val handle = requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE).getString("user_handle", null)?: "Md_Anas_Ali_Usmani"

        lifecycleScope.launch {
            val response = try {
                RetrofitInstance.api.getUpsolveContests(handle)
            } catch (e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                return@launch
            }
            if (response.isSuccessful && response.body() != null && response.body()!!.status == "OK") {
                val upcomingContests = response.body()!!.result.asReversed()
                UpsolveContestAdapter.upsolve_contests = upcomingContests
            } else {
                Log.e(TAG, "Response not successful")
            }
            Log.e(TAG, response.body().toString())
        }
    }

}