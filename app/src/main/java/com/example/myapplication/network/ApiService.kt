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
    
    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") token:String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<PostStory>
}