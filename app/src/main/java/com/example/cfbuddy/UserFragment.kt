package com.example.cfbuddy

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.cfbuddy.databinding.FragmentUnsolvedBinding
import com.example.cfbuddy.databinding.FragmentUserBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class UserFragment : Fragment() {

    lateinit var binding: FragmentUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserBinding.inflate(layoutInflater)
        val handle = requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE).getString("user_handle", null)?: "Md_Anas_Ali_Usmani"

        lifecycleScope.launch {
            val response = try {
                RetrofitInstance.api.getUserInfo(handle)
            } catch (e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                return@launch
            }
            if (response.isSuccessful && response.body() != null && response.body()!!.status == "OK") {
                var result = response.body()!!.result.first()
                binding.tvName.text = "${result.firstName} ${result.lastName}"
                binding.tvHandle.text = result.handle
                binding.tvOrganization.text = result.organization
                binding.tvTitle.text = result.rank
                binding.tvRating.text = result.rating.toString()
                binding.tvMaxRating.text = result.maxRating.toString()
                binding.tvCityCountry.text = "City: ${result.city}, ${result.country}"
                binding.tvLastOnline.text = result.lastOnlineTimeSeconds.toString()
                binding.tvFriends.text = result.friendOfCount.toString()
                binding.tvContribution.text = result.contribution.toString()
                binding.tvRegistration.text = result.registrationTimeSeconds.toString()
            } else {
                Log.e(TAG, "Response not successful")
            }
            Log.e(TAG, response.body().toString())
        }
    }
}