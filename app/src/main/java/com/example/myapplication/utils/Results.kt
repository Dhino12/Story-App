package com.example.myapplication.utils

sealed class Results <out R> private constructor(){
    data class Success<out T>(val data: T): Results<T>()
    data class Error(val data: String): Results<Nothing>()
    object Loading : Results<Nothing>()

}