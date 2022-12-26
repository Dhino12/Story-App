package com.example.myapplication.views.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.database.StoryRepository
import com.example.myapplication.getOrAwaitValue
import com.example.myapplication.model.Stories
import com.example.myapplication.utils.Results
import com.example.myapplication.utls.DataDummy
import com.example.myapplication.viewmodel.StoryViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StoryLoadLocationTest {
    companion object {
        private const val TOKEN = "Bearer TOKEN"
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyViewModel: StoryViewModel
    private val dummyLoadStoryLocation = DataDummy.generateDummyStoryWithMapsResponse()

    @Before
    fun setup() {
        storyViewModel = StoryViewModel(storyRepository)
    }

    @Test
    fun `when get story with maps should not null return success`() {
        val expectStory = MutableLiveData<Results<Stories>>()
        expectStory.value = Results.Success(dummyLoadStoryLocation)
        Mockito.`when`(storyViewModel.loadStoryLocation(TOKEN)).thenReturn(expectStory)

        val actualStoryLocation = storyViewModel.loadStoryLocation(TOKEN).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoryMaps(TOKEN)
        Assert.assertNotNull(actualStoryLocation)
        Assert.assertTrue(actualStoryLocation is Results.Success)
    }
}