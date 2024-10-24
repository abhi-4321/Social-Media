package com.example.socialmedia.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmedia.databinding.ActivitySplashBinding
import com.example.socialmedia.ui.login.LoginActivity
import com.example.socialmedia.ui.main.MainActivity
import com.example.socialmedia.util.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            if (tokenManager.getSession())
                startActivity(Intent(this, MainActivity::class.java))
            else
                startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 1500)
    }
}