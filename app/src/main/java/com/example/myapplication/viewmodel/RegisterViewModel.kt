package com.example.myapplication.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.network.ApiConfig
import com.example.myapplication.response.ResponseRegister
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(val context: Context): ViewModel() {
    val loading = MutableLiveData(View.GONE)
    val error = MutableLiveData("")
    private val tagClassName = RegisterViewModel::class.java.simpleName

    fun register(name: String, email: String, password: String) {
        loading.postValue(View.VISIBLE)

        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                when(response.code()) {
                    400 -> error.postValue(response.message())
                    201 -> error.postValue(response.message())
                    else -> error.postValue("ERROR ${response.code()} : ${response.errorBody()}")
                }
                loading.postValue(View.GONE)
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                loading.postValue(View.GONE)
                Log.e(tagClassName, "onFailure call: ${t.message}")
                error.postValue(t.message)
            }
        })
    }
}