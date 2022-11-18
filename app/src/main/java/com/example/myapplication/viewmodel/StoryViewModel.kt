package com.example.myapplication.viewmodel

import android.content.ClipDescription
import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.PostStory
import com.example.myapplication.model.Stories
import com.example.myapplication.model.Story
import com.example.myapplication.network.ApiConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryViewModel(val context: Context): ViewModel() {
    val loading = MutableLiveData(View.GONE)
    val storyList = MutableLiveData<List<Story>>()
    val resultPostUpload = MutableLiveData(false)
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

    fun postStory(token: String, image: File, description: String) {
        loading.postValue(View.VISIBLE)

        val descStory = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            image.name,
            requestImageFile
        )

        val client = ApiConfig.getApiService().postStory(token, descStory, imageMultipart)
        client.enqueue(object : Callback<PostStory> {
            override fun onResponse(call: Call<PostStory>, response: Response<PostStory>) {
                when(response.code()) {
                    401 -> error.postValue(response.message())
                    413 -> error.postValue(response.message())
                    201 -> resultPostUpload.postValue(true)
                    else -> error.postValue("ERROR ${response.code()} : ${response.errorBody()}")
                }
                loading.postValue(View.GONE)
            }

            override fun onFailure(call: Call<PostStory>, t: Throwable) {
                loading.postValue(View.GONE)
                Log.e(TAG.simpleName, "onFailure.......... : ${t.message}")
                error.postValue(t.message)
            }

        })
    }
}