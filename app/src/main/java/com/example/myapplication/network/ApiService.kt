package com.example.myapplication.network

import com.example.myapplication.response.ResponseLogin
import com.example.myapplication.response.ResponseRegister
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
}