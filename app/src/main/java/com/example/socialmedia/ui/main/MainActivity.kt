package com.example.socialmedia.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.socialmedia.R
import com.example.socialmedia.databinding.ActivityMainBinding
import com.example.socialmedia.viewmodel.MainViewModel
import com.example.socialmedia.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findNavController(R.id.fragment_container)

        ViewModelProvider(this)[MainViewModel::class.java]
    }
}