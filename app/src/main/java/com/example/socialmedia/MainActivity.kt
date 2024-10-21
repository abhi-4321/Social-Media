package com.example.socialmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.socialmedia.network.RetrofitService
import com.example.socialmedia.repository.LoginRepository
import com.example.socialmedia.util.TokenManager
import com.example.socialmedia.viewmodel.SignUpViewModel
import com.example.socialmedia.viewmodel.SignUpViewModelFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findNavController(R.id.fragment_container)

        val tokenManager = TokenManager(applicationContext)

        val loginApi = RetrofitService.getInstance()
        val repository = LoginRepository(loginApi)
        ViewModelProvider(this,SignUpViewModelFactory(repository))[SignUpViewModel::class.java]
    }
}