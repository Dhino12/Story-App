package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.database.StoryRepository
import com.example.myapplication.model.Story

class StoryPagerViewModel(private val storyRepository: StoryRepository) :ViewModel() {
    fun getStory(token:String): LiveData<PagingData<Story>> =
        storyRepository.getStory(token).cachedIn(viewModelScope)
}