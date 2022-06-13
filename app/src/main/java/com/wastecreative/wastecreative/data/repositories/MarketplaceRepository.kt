package com.wastecreative.wastecreative.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.wastecreative.wastecreative.data.database.MarketplaceEntity
import com.wastecreative.wastecreative.data.database.WasteCreativeDB
import com.wastecreative.wastecreative.data.models.*
import com.wastecreative.wastecreative.data.network.ApiService
import com.wastecreative.wastecreative.data.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MarketplaceRepository(
    private val wasteCreativeDB: WasteCreativeDB,
    private val apiService: ApiService,
) {
    fun getPosts(): LiveData<PagingData<MarketplaceEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = MarketplaceRemoteMediator(wasteCreativeDB, apiService),
            pagingSourceFactory = {
                wasteCreativeDB.marketDao().getAllMarketplace()
            }
        ).liveData
    }
    suspend fun refresh() {
        wasteCreativeDB.marketDao().deleteAll()
        wasteCreativeDB.marketRemoteKeysDao().deleteRemoteKeys()

    }
    suspend fun fetchPostsList(): Flow<Result<List<Marketplace>>> {
        return flow {
            emit(Result.Loading)
            try {
                val result = apiService.getPostsList()
                emit(Result.Success(result.data))
            } catch (e: Exception) {
                Log.d("CraftRepository", "fetchCraftList: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun fetchPostDetail(id: Int): Flow<Result<MarketplaceDetail>>{
        return flow {
            emit(Result.Loading)
            try {
                val result = apiService.getPostsDetail(id)
                emit(Result.Success(result.data))
            } catch (e: Exception) {
                Log.d("CraftRepository", "fetchCraftList: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun fetchComment(id: Int): Flow<Result<List<MarketplaceComment>>> {
        return flow {
            emit(Result.Loading)
            try {
                val result = apiService.getCommentList(id)
                emit(Result.Success(result.comments))
            } catch (e: Exception) {
                Log.d("CraftRepository", "fetchCraftList: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
//    suspend fun fetchPostSearchResult(query: String): Flow<Result<List<Craft>>?> {
//        return flow {
//            emit(Result.Loading)
//            try {
//                val result = apiService.getSearcCraftList(query)
//                emit(Result.Success(result))
//            } catch (e: Exception) {
//                Log.d("CraftRepository", "fetchCraftSearchResult: ${e.message.toString()} ")
//                emit(Result.Error(e.message.toString()))
//            }
//        }.flowOn(Dispatchers.IO)
//    }

    suspend fun postComment(
        comment: RequestBody,
        id_post: RequestBody,
        user_id: RequestBody,
    ): Flow<Result<UploadResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val result = apiService.postComment(comment, id_post, user_id)
                emit(Result.Success(result))
            } catch (e: Exception) {
                Log.d("StoryRepository", "UploadStory: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postMarketplace(
        file: MultipartBody.Part,
        title: RequestBody,
        desc: RequestBody,
        price: RequestBody,
        address: RequestBody,
        user_id: RequestBody,
    ): Flow<Result<UploadResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val result = apiService.uploadMarketplace(file, title, desc, price, address, user_id)
                emit(Result.Success(result))
            } catch (e: Exception) {
                Log.d("StoryRepository", "UploadStory: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
    companion object {
        @Volatile
        private var instance: MarketplaceRepository? = null
        fun getInstance(
            wasteCreativeDB: WasteCreativeDB,
            apiService: ApiService,
        ): MarketplaceRepository =
            instance ?: synchronized(this) {
                instance ?: MarketplaceRepository(wasteCreativeDB, apiService)
            }.also { instance = it }
    }
}