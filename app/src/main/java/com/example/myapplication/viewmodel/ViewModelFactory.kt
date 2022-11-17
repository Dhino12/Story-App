package com.example.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.Repository

class ViewModelFactory private constructor(private val repository: Repository):
    ViewModelProvider.NewInstanceFactory() {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                return LoginViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown viewmodel class : " + modelClass.name )
        }
    }
}