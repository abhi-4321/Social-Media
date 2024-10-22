package com.example.socialmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.model.LoginRequest
import com.example.socialmedia.model.LoginResponse
import com.example.socialmedia.model.SignUpRequest
import com.example.socialmedia.repository.LoginRepository
import com.example.socialmedia.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<LoginResponse>>
        get() = loginRepository.loginResponseLiveData

    val loginResponseLiveData : LiveData<NetworkResult<LoginResponse>>
        get()= loginRepository.loginResponseLiveData

    fun registerUser(signUpRequest: SignUpRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.register(signUpRequest)
        }
    }

    fun loginUser(loginRequest: LoginRequest){
        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.login(loginRequest)
        }
    }
}