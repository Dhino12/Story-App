package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class Stories (

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("listStory")
    val listStory: List<Story>
)