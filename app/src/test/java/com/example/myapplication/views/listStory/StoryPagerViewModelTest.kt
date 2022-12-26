package com.example.myapplication.views.listStory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.myapplication.MainCoroutineRule
import com.example.myapplication.getOrAwaitValue
import com.example.myapplication.model.Stories
import com.example.myapplication.model.Story
import com.example.myapplication.utls.DataDummy
import com.example.myapplication.viewmodel.StoryPagerViewModel
import com.example.myapplication.viewmodel.StoryViewModel
import com.example.myapplication.views.main.listFragment.ListStoryAdapter
import com.example.myapplication.views.register.RegisterViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryPagerViewModelTest {
    companion object {
        private const val TOKEN = "Bearer TOKEN"
    }
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutinesRules = MainCoroutineRule()

    @Mock
    private lateinit var storyPagerViewModel: StoryPagerViewModel

    @Test
    fun `when get story should not null`() = mainCoroutinesRules.runBlockingTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<Story> = PagedTestHelperDataSource.snapshot(dummyStory)
        val story = MutableLiveData<PagingData<Story>>()

        story.value = data
        Mockito.`when`(storyPagerViewModel.getStory(TOKEN)).thenReturn(story)

        val actualStoryPager = storyPagerViewModel.getStory(TOKEN).getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = listCallback,
            mainDispatcher = mainCoroutinesRules.dispatcher,
            workerDispatcher = mainCoroutinesRules.dispatcher
        )
        differ.submitData(actualStoryPager)

        advanceUntilIdle()

        Mockito.verify(storyPagerViewModel).getStory(TOKEN)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
    }
}

class PagedTestHelperDataSource private constructor(private val items: List<Story>) :
    PagingSource<Int, LiveData<List<Story>>>() {

    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int? {
        return  0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0 ,1)
    }
}

val listCallback = object: ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}

    override fun onRemoved(position: Int, count: Int) {}

    override fun onMoved(fromPosition: Int, toPosition: Int) {}

    override fun onChanged(position: Int, count: Int, payload: Any?) {}

}