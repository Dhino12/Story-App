package com.example.myapplication.network

import com.example.myapplication.model.Stories
import com.example.myapplication.response.ResponseLogin
import com.example.myapplication.response.ResponseRegister
import retrofit2.Call
import retrofit2.http.*

interface ApiService{

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field(encoded = false, value = "email") email: String,
        @Field("password") password: String
    ): Call<ResponseLogin>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ):Call<ResponseRegister>

    @GET("stories")
    fun getListStory(
        @Header("Authorization") token: String,
        @Query("size") size: Int
    ): Call<Stories>
}