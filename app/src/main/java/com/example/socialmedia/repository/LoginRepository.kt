package com.example.socialmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.socialmedia.model.LoginRequest
import com.example.socialmedia.model.LoginResponse
import com.example.socialmedia.model.SignUpRequest
import com.example.socialmedia.network.LoginApi
import com.example.socialmedia.util.NetworkResult
import org.json.JSONObject
import retrofit2.Response

class LoginRepository (private val loginApi: LoginApi) {

    private  val _loginResponseLiveData = MutableLiveData<NetworkResult<LoginResponse>>()
    val loginResponseLiveData : LiveData<NetworkResult<LoginResponse>>
        get() = _loginResponseLiveData

    suspend fun register(signUpRequest: SignUpRequest){
        _loginResponseLiveData.postValue(NetworkResult.Loading())
        val response = loginApi.register(signUpRequest)
        handleResponse(response)
    }

    suspend fun login(loginRequest: LoginRequest){
        _loginResponseLiveData.postValue(NetworkResult.Loading())
        val response = loginApi.login(loginRequest)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<LoginResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _loginResponseLiveData.postValue(NetworkResult.Success(response.body()!!))

        } else if (response.errorBody() != null) {
            val error0bj = JSONObject(response.errorBody()!!.charStream().readText())
            _loginResponseLiveData.postValue(NetworkResult.Error(error0bj.getString("message")))
        } else {
            _loginResponseLiveData.postValue(NetworkResult.Error("Something went wrong!!"))
        }
    }

}