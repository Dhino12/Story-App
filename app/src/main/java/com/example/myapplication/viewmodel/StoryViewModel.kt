package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.database.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel (private val storyRepository: StoryRepository): ViewModel() {
    fun loadStoryLocation(token: String) =
        storyRepository.getStoryMaps(token)

    fun postStory(
        token: String,
        image: MultipartBody.Part,
        description: RequestBody,
        lat:Double = 1.1,
        lon: Double = 1.1) =
            storyRepository.postStory( token, image, description, lat,lon)
}