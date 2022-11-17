package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class LoginUser (
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("token")
    val token:String
)