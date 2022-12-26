package com.example.myapplication.database

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.myapplication.model.Story
import com.example.myapplication.model.StoryRemoteKeys
import com.example.myapplication.network.ApiService

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator (
    private val database: StoryDb,
    private val apiService: ApiService,
    private val token: String
        ) : RemoteMediator<Int, Story>() {

            private companion object {
                const val PAGE_INDEX_INIT = 1
            }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Story>): MediatorResult {
        val page = when(loadType) {
            LoadType.REFRESH -> {
                PAGE_INDEX_INIT
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKeys = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKeys
            }
        }

        return try {
            val responseData = apiService.getListStory(token, page, state.config.pageSize).listStory

            val endOfPagingReached = responseData.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.storyRemoteKeysDao().deleteRemoteKeys()
                    database.storyDao().deleteAll()
                }

                val prevKey = if(page == 1) null else page - 1
                val nextKey = if(endOfPagingReached) null else page + 1
                val keys = responseData.map { story ->
                    StoryRemoteKeys(id = story.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.storyRemoteKeysDao().insertAll(keys)
                database.storyDao().insertStory(responseData)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPagingReached)
        } catch (exception: Exception) {
            Log.e("testErro", exception.toString())
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Story>): StoryRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.storyRemoteKeysDao()?.getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Story>): StoryRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.storyRemoteKeysDao().getRemoteKeysId(data.id)
        }
    }
}