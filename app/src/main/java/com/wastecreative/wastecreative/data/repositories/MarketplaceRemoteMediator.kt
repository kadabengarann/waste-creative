package com.wastecreative.wastecreative.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.wastecreative.wastecreative.data.database.*
import com.wastecreative.wastecreative.data.network.ApiService


@OptIn(ExperimentalPagingApi::class)
class MarketplaceRemoteMediator(
    private val database: WasteCreativeDB,
    private val apiService: ApiService
) : RemoteMediator<Int, MarketplaceEntity>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MarketplaceEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.getPosts(page, state.config.pageSize)

            val endOfPaginationReached = responseData.data.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.marketDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.data.map {
                    MarketplaceRemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                val marketplaceData = responseData.data.map {
                    MarketplaceEntity(
                        id = it.id,
                        it.pengguna_id,
                        it.userName,
                        it.userPhoto,
                        it.like,
                        it.judul,
                        it.foto,
                        it.komentar,
                    )
                }
                database.marketRemoteKeysDao().insertAll(keys)
                database.marketDao().insertMarketplace(marketplaceData)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MarketplaceEntity>): MarketplaceRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.marketRemoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MarketplaceEntity>): MarketplaceRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.marketRemoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MarketplaceEntity>): MarketplaceRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.marketRemoteKeysDao().getRemoteKeysId(id)
            }
        }
    }
}