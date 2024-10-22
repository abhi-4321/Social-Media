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
import com.example.socialmedia.databinding.FragmentLoginBinding
import com.example.socialmedia.model.LoginRequest
import com.example.socialmedia.ui.main.MainActivity
import com.example.socialmedia.util.NetworkResult
import com.example.socialmedia.util.TokenManager
import com.example.socialmedia.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        val loginViewModel = ViewModelProvider(requireActivity())[SignUpViewModel::class.java]

        loginViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.access)
                    startActivity(Intent(requireContext(),MainActivity::class.java))
                }

                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val validation = validateUserInput()
            if (validation.first) {
                loginViewModel.loginUser(getUserRequest())
            } else {
                binding.txtError.text = validation.second
            }
        }
        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }

        return binding.root
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userrequest = getUserRequest()
        return validate(userrequest.email, userrequest.password)
    }

    private fun getUserRequest(): LoginRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        return LoginRequest(emailAddress, password)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validate(emailAddress: String, password: String): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)) {
            result = Pair(false, "Please provide the credentials")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            result = Pair(false, "Please provide valid email")
        } else if (password.length <= 3) {
            result = Pair(false, "password length should be greater than 5")
        }
        return result
    }
}