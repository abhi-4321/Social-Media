package com.example.socialmedia.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.socialmedia.databinding.FragmentSignUpBinding
import com.example.socialmedia.model.SignUpRequest
import com.example.socialmedia.ui.main.MainActivity
import com.example.socialmedia.util.NetworkResult
import com.example.socialmedia.util.TokenManager
import com.example.socialmedia.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding get() = _binding!!

    @Inject
    lateinit var tokenManager: TokenManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        val signUpViewModel = ViewModelProvider(requireActivity())[SignUpViewModel::class.java]

        // error handling
        signUpViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.access)
                    tokenManager.saveSession(binding.txtUsername.text.toString())
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }

                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }

        //click listeners
        binding.btnSignUp.setOnClickListener {
            val validation = validateUserInput()
            if (validation.first) {
                signUpViewModel.registerUser(getUserRequest())
            } else {
                binding.txtError.text = validation.second
            }
        }
        binding.btnLogin.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userrequest = getUserRequest()
        return validate(
            userrequest.name,
            userrequest.email,
            userrequest.password,
            userrequest.password2
        )
    }

    private fun getUserRequest(): SignUpRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        val userName = binding.txtUsername.text.toString()
        return SignUpRequest(userName, emailAddress, password, password)
    }

    private fun validate(
        userName: String,
        emailAddress: String,
        password: String,
        confirmPassword: String
    ): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(
                password
            ) || TextUtils.isEmpty(confirmPassword)
        ) {
            result = Pair(false, "Please provide the credentials")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            result = Pair(false, "Please provide valid email")
        } else if (password.length <= 3) {
            result = Pair(false, "password length should be greater than 5")
        }
        return result
    }
}