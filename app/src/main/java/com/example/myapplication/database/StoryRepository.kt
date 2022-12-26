package com.example.myapplication.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.myapplication.model.PostStory
import com.example.myapplication.model.Stories
import com.example.myapplication.model.Story
import com.example.myapplication.network.ApiService
import com.example.myapplication.response.ResponseLogin
import com.example.myapplication.response.ResponseRegister
import com.example.myapplication.utils.Results
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository (
    private val storyDatabase: StoryDb,
    private val apiService: ApiService,
    private val token: String
        ) {
    fun getStory(token: String): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = { storyDatabase.storyDao().getAllStory() }
        ).liveData
    }

    fun getStoryMaps(token:String):LiveData<Results<Stories>> =
        liveData {
            emit(Results.Loading)
            try {
                val response = apiService.getStoryListLocation(token, 30)
                emit(Results.Success(response))
            } catch (e:Exception) {
                Log.e("StoryMap repo", e.message.toString())
                emit(Results.Error(e.message.toString()))
            }
        }

    fun login(email: String, password: String): LiveData<Results<ResponseLogin>> =
        liveData {
            emit(Results.Loading)
            try {
                val response = apiService.login(email, password)
                emit(Results.Success(response))
            } catch (e: Exception) {
                Log.e("login repo", e.message.toString())
                emit(Results.Error(e.message.toString()))
            }
        }

    fun register(email: String, name: String, password: String): LiveData<Results<ResponseRegister>> =
        liveData {
            emit(Results.Loading)
            try {
                val response = apiService.register(name, email, password)
                emit(Results.Success(response))
            } catch (e: Exception) {
                Log.e("register repo", e.message.toString())
                emit(Results.Error(e.message.toString()))
            }
        }

    fun postStory(token: String, file: MultipartBody.Part, description: RequestBody, lat: Double, lon: Double): LiveData<Results<PostStory>> =
        liveData {
            emit(Results.Loading)

            try {
                var response = apiService.postStory(token, description, file, lat, lon)
                emit(Results.Success(response))
            } catch (e: Exception) {
                Log.e("postImage repo", e.message.toString())
                emit(Results.Error(e.message.toString()))
            }
        }
}