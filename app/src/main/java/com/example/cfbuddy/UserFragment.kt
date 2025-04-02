package com.example.cfbuddy

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import java.util.Date

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                val result = response.body()!!.result.first()
                val dateLastOnline = Date(result.lastOnlineTimeSeconds * 1000)
                val dateRegistration = Date(result.registrationTimeSeconds * 1000)
                val dateLastOnlineFormat = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm a", dateLastOnline)
                val dateRegistrationFormat = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm a", dateRegistration)
                binding.tvName.text = "${result.firstName} ${result.lastName}"
                binding.tvHandle.text = result.handle
                binding.tvOrganization.text = result.organization
                binding.tvTitle.text = result.rank
                binding.tvRating.text = "Rating: ${result.rating}"
                binding.tvMaxRating.text = "Max Rating: ${result.maxRating}"
                binding.tvCityCountry.text = "City: ${result.city}, ${result.country}"
                binding.tvLastOnline.text = "Last Online: $dateLastOnlineFormat"
                binding.tvFriends.text = "Friend of: ${result.friendOfCount} handles"
                binding.tvContribution.text = "Contribution: ${result.contribution}"
                binding.tvRegistration.text = "Registered: $dateRegistrationFormat"
            } else {
                Log.e(TAG, "Response not successful")
            }
            Log.e(TAG, response.body().toString())
        }

        binding.btnResetHandle.setOnClickListener {
            val sharedPref = requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
            sharedPref.edit().remove("user_handle").apply()
            startActivity(Intent(this@UserFragment.requireContext(), HandleActivity::class.java))
            activity?.finish()
        }
    }
}