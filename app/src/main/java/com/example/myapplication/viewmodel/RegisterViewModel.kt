package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.database.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun register(email: String, name: String, password: String) =
        storyRepository.register(email, name, password)

}