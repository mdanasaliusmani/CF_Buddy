package com.example.cfbuddy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.cfbuddy.databinding.ActivityHandleBinding
import com.example.cfbuddy.databinding.ActivityUpsolveContestProblemsBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class HandleActivity : AppCompatActivity() {

    private lateinit var bindings: ActivityHandleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = ActivityHandleBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(bindings.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bindings.btnHandle.setOnClickListener {
            Toast.makeText(this, "Verifying handle", Toast.LENGTH_SHORT).show()
            val handle = bindings.etHandle.text.toString().trim()
            if (handle.isNotEmpty()) {
                lifecycleScope.launch {
                    val response = try {
                        RetrofitInstance.api.getUserInfo(handle)
                    } catch (e: IOException) {
                        Log.e(TAG, "IOException, you might not have internet connection")
                        Toast.makeText(this@HandleActivity, "No internet connection", Toast.LENGTH_SHORT).show()
                        return@launch
                    } catch (e: HttpException) {
                        Log.e(TAG, "HttpException, unexpected response")
                        Toast.makeText(this@HandleActivity, "Invalid handle", Toast.LENGTH_SHORT).show()
                        return@launch
                    }
                    if (response.isSuccessful && response.body() != null && response.body()!!.status == "OK") {
                        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                        sharedPref.edit().putString("user_handle", handle).apply()
                        Toast.makeText(this@HandleActivity, "Handle saved", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@HandleActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@HandleActivity, "Invalid handle", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter your handle", Toast.LENGTH_SHORT).show()
            }
        }
    }
}