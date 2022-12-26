package com.example.myapplication.data

import com.example.myapplication.model.PostStory
import com.example.myapplication.model.Stories
import com.example.myapplication.network.ApiService
import com.example.myapplication.response.ResponseLogin
import com.example.myapplication.response.ResponseRegister
import com.example.myapplication.utls.DataDummy
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService: ApiService {
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private val dummyStoryResponse = DataDummy.generateDummyStoryWithMapsResponse()
    private val dummyListStoryLocationResponse = DataDummy.generateDummyStoryWithMapsResponse()
    private val dummyAddNewStoryResponse = DataDummy.generateDummyAddNewStoryResponse()
    override suspend fun login(email: String, password: String): ResponseLogin {
        return dummyLoginResponse
    }

    override suspend fun register(name: String, email: String, password: String): ResponseRegister {
        return dummyRegisterResponse
    }

    override suspend fun getListStory(token: String, page: Int, size: Int): Stories {
        return dummyStoryResponse
    }

    override suspend fun getStoryListLocation(token: String, size: Int): Stories {
        return dummyListStoryLocationResponse
    }

    override suspend fun postStory(
        token: String,
        description: RequestBody,
        file: MultipartBody.Part,
        lat: Double,
        lng: Double
    ): PostStory {
        return dummyAddNewStoryResponse
    }


}