package com.example.myapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.Repository
import com.example.myapplication.network.ApiConfig
import com.example.myapplication.response.ResponseLogin

class LoginViewModel (private val repository: Repository): ViewModel() {
//    fun login(email:String, password: String) = repository.login(email, password)
    val loginResult = MutableLiveData<ResponseLogin>()

    suspend fun login(email:String, password: String) {
        val client = ApiConfig.getApiService().login(email, password)
        client.loginResult
    }
}