package com.example.myapplication.views.postStory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.database.StoryRepository
import com.example.myapplication.getOrAwaitValue
import com.example.myapplication.model.PostStory
import com.example.myapplication.utils.Results
import com.example.myapplication.utls.DataDummy
import com.example.myapplication.viewmodel.StoryViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    companion object {
        private const val TOKEN = "Bearer TOKEN"
        private const val LAT = 1.11
        private const val LNG = 1.41
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyViewModel: StoryViewModel
    private val dummyAddStory = DataDummy.generateDummyAddNewStoryResponse()

    @Before
    fun setup() {
        storyViewModel = StoryViewModel(storyRepository)
    }

    @Test
    fun `when upload should not null return success`() {
        val descriptionReqBody = "deskripsi gambar".toRequestBody("text/plain".toMediaType())
        val file = mock(File::class.java)
        val imageFileRequest = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            "thumbnail",
            imageFileRequest
        )

        val expectStoryPost = MutableLiveData<Results<PostStory>>()
        expectStoryPost.value = Results.Success(dummyAddStory)
        Mockito.`when`(storyViewModel.postStory(TOKEN, imageMultipart, descriptionReqBody, LAT, LNG)).thenReturn(expectStoryPost)

        val actualStory = storyViewModel.postStory(TOKEN, imageMultipart, descriptionReqBody, LAT, LNG).getOrAwaitValue()
        Mockito.verify(storyRepository).postStory(TOKEN, imageMultipart, descriptionReqBody, LAT, LNG)

        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Results.Success)

    }
}