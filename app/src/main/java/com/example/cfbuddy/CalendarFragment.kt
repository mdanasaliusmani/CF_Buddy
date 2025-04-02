package com.example.cfbuddy

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cfbuddy.databinding.FragmentCalendarBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

const val TAG = "CalendarFragment"

class CalendarFragment : Fragment() {

    lateinit var binding: FragmentCalendarBinding
    private lateinit var ContestListAdapter: ContestListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupRecyclerView() = binding.rvCalendar.apply {
        ContestListAdapter = ContestListAdapter()
        adapter = ContestListAdapter
        layoutManager = LinearLayoutManager(requireContext())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding = FragmentCalendarBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            val response = try {
                RetrofitInstance.api.getUpcomingContests()
            } catch (e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                return@launch
            }
            if (response.isSuccessful && response.body() != null && response.body()!!.status == "OK") {
                val upcomingContests = response.body()!!.result.filter { it.phase == "BEFORE" }
                ContestListAdapter.contests = upcomingContests.asReversed()
            } else {
                Log.e(TAG, "Response not successful")
            }
            Log.e(TAG, response.body().toString())
        }
    }
}