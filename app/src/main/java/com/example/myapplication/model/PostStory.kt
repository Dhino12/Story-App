package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class PostStory (
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)