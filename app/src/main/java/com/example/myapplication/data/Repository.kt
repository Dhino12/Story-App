package com.example.myapplication.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.myapplication.network.ApiService
import com.example.myapplication.response.ResponseLogin

class Repository(private val apiService: ApiService) {
    fun login(email: String, password: String): LiveData<Result<ResponseLogin>> = liveData {
        try {
            val response = apiService.login(email,password)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}