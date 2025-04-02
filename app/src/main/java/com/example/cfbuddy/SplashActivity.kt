package com.example.cfbuddy

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
            val userHandle = sharedPref.getString("user_handle", null)
            val intent : Intent
            if (userHandle.isNullOrEmpty()) {
                intent = Intent(this, HandleActivity::class.java)
            } else {
                intent = Intent(this, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        },3000)
    }
}