package com.example.myapplication.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.LoginUser
import com.example.myapplication.network.ApiConfig
import com.example.myapplication.response.ResponseLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel (private val context: Context): ViewModel() {
    val loginResult = MutableLiveData<ResponseLogin>()
    val error = MutableLiveData("")
    val loading = MutableLiveData(View.GONE)
    private val tagClassName = LoginViewModel::class.java.simpleName

    fun login(email: String, password: String) {
        Log.d(tagClassName, email)
        val client = ApiConfig.getApiService().login(email, password)

        client.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                when(response.code()) {
                    400 -> error.postValue(response.message())
                    401 -> error.postValue(response.message())
                    200 -> loginResult.postValue(response.body())
                    else -> error.postValue("Error ${response.code()} : ${response.message()}")
                }
                loading.postValue(View.GONE)
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                loading.postValue(View.GONE)
                Log.e(tagClassName, "onFailure... : ${t.message}")
                error.postValue(t.message)
            }
        })
    }
}