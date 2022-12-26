package com.example.myapplication.network

import com.example.myapplication.model.PostStory
import com.example.myapplication.model.Stories
import com.example.myapplication.response.ResponseLogin
import com.example.myapplication.response.ResponseRegister
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService{

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseLogin

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ):ResponseRegister

    @GET("stories")
    suspend fun getListStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Stories

    @GET("stories?location=1")
    suspend fun getStoryListLocation(
        @Header("Authorization") token: String,
        @Query("size") size: Int
    ): Stories

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Header("Authorization") token:String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("lat") lat: Double,
        @Part("lon") lon: Double
    ): PostStory
}