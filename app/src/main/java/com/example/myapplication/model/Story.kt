package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class Story (
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("lon")
    val lon: Any,

    @field:SerializedName("lat")
    val lat: Any,

    @field:SerializedName("id")
    val id: String
)