package com.example.myapplication.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Stories
import com.example.myapplication.model.Story
import com.example.myapplication.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(val context: Context): ViewModel() {
    val loading = MutableLiveData(View.GONE)
    val storyList = MutableLiveData<List<Story>>()
    var error = MutableLiveData("")
    private val TAG = StoryViewModel::class.java

    fun getStory(token: String) {
        loading.postValue(View.VISIBLE)
        val client = ApiConfig.getApiService().getListStory(token, 30)
        client.enqueue(object : Callback<Stories> {
            override fun onResponse(call: Call<Stories>, response: Response<Stories>) {
                if (response.isSuccessful) {
                    storyList.postValue(response.body()?.listStory)
                } else {
                    error.postValue("ERROR ${response.code()} : ${response.message()}")
                }
                loading.postValue(View.GONE)
            }

            override fun onFailure(call: Call<Stories>, t: Throwable) {
                loading.postValue(View.GONE)
                Log.e(TAG.simpleName, "onFailure : ${t.message}")
                error.postValue("${t.message}")
            }
        })
    }
}