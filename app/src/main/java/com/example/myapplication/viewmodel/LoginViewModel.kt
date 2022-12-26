package com.example.myapplication.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.database.StoryRepository
import com.example.myapplication.model.LoginUser
import com.example.myapplication.network.ApiConfig
import com.example.myapplication.response.ResponseLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun login(email: String, password: String) = storyRepository.login(email,password)
}