package com.example.myapplication.response

import com.example.myapplication.model.LoginUser
import com.google.gson.annotations.SerializedName

data class ResponseLogin (
    @field:SerializedName("loginResult")
    val loginResult: LoginUser,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)