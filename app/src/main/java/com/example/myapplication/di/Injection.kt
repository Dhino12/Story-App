package com.example.myapplication.di

import android.content.Context
import android.util.Log
import com.example.myapplication.database.StoryDb
import com.example.myapplication.database.StoryRepository
import com.example.myapplication.network.ApiConfig

object Injection {
    fun provideRepository(context: Context, token: String): StoryRepository {
        val database = StoryDb.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService, token)
    }
}