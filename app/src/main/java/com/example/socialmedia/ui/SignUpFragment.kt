package com.example.socialmedia.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.socialmedia.R
import com.example.socialmedia.databinding.FragmentLoginBinding
import com.example.socialmedia.databinding.FragmentSignUpBinding
import com.example.socialmedia.model.SignUpRequest
import com.example.socialmedia.network.RetrofitService
import com.example.socialmedia.repository.LoginRepository
import com.example.socialmedia.util.NetworkResult
import com.example.socialmedia.util.TokenManager
import com.example.socialmedia.viewmodel.SignUpViewModel
import com.example.socialmedia.viewmodel.SignUpViewModelFactory

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater,container,false)

        val tokenManager = TokenManager(requireContext().applicationContext)
        val loginApi = RetrofitService.getInstance()
        val repository = LoginRepository(loginApi)
        val signUpViewModel  = ViewModelProvider(requireActivity(),SignUpViewModelFactory(repository))[SignUpViewModel::class.java]

        // error handling
        signUpViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible =false
            when(it){
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.access)
                    Log.d("LoginResponse", "${it.data}")
                    findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToHomeFragment(it.data.access))
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
            if (validation.first){
                signUpViewModel.registerUser(getUserRequest())
            }else{
                binding.txtError.text = validation.second
            }
        }
        binding.btnLogin.setOnClickListener{
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userrequest = getUserRequest()
        return validate(userrequest.name, userrequest.email,userrequest.password)
    }
    private fun getUserRequest(): SignUpRequest{
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        val userName = binding.txtUsername.text.toString()
        return SignUpRequest(userName,emailAddress,password,password)
    }

    private fun validate(userName: String, emailAddress : String, password : String) : Pair<Boolean, String>{
        var result = Pair(true,"")
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)){
            result = Pair(false, "Please provide the credentials")
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            result = Pair(false, "Please provide valid email")
        }
        else if(password.length <= 3){
            result = Pair(false, "password length should be greater than 5")
        }
        return result
    }
}