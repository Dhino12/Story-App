package com.example.myapplication.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.MainCoroutineRule
import com.example.myapplication.network.ApiService
import com.example.myapplication.utls.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        apiService = FakeApiService()
    }

    companion object {
        private const val TOKEN = "Bearer TOKEN_STORY"
        private const val NAME = "joko"
        private const val EMAIL = "joko121@gmail.com"
        private const val PASSWORD = "joko_123"
        private const val LAT = 1.1
        private const val LNG = 1.4
    }

    @Test
    fun getStoriesTest() = mainCoroutineRules.runBlockingTest {
        val actualStory = apiService.getListStory(TOKEN, 1, 30)
        val expectStory = DataDummy.generateDummyStoryResponse()
        assertNotNull(actualStory)
        assertEquals(expectStory.size, actualStory.listStory.size)
    }

    @Test
    fun registerTest() = mainCoroutineRules.runBlockingTest {
        val actualUser = apiService.register(NAME, EMAIL, PASSWORD)
        val expectUser = DataDummy.generateDummyRegisterResponse()

        assertNotNull(actualUser)
        assertEquals(expectUser.message, actualUser.message)
    }

    @Test
    fun loginTest() = mainCoroutineRules.runBlockingTest {
        val actualUser = apiService.login(EMAIL, PASSWORD)
        val expectUser = DataDummy.generateDummyLoginResponse()

        assertNotNull(actualUser)
        assertEquals(expectUser.message, actualUser.message)
    }

    @Test
    fun getStoryLocationTest() = mainCoroutineRules.runBlockingTest {
        val actualStoryLocation = apiService.getStoryListLocation(TOKEN, 30)
        val expectStoryLocation = DataDummy.generateDummyStoryWithMapsResponse()

        assertNotNull(actualStoryLocation)
        assertEquals(expectStoryLocation.listStory.size, actualStoryLocation.listStory.size)
    }

    @Test
    fun postStoryTest() = mainCoroutineRules.runBlockingTest {
        val description = "deskripsi gambar".toRequestBody("text/plain".toMediaType())
        val file = mock(File::class.java)
        val imageFileRequest = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            "thumbnail",
            imageFileRequest
        )

        val actualStoryPost = DataDummy.generateDummyAddNewStoryResponse()
        val expectStoryPost = apiService.postStory(TOKEN, description, imageMultipart, LAT, LNG)

        assertNotNull(actualStoryPost)
        assertEquals(expectStoryPost.message, actualStoryPost.message)
    }
}