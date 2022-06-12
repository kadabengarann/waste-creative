package com.wastecreative.wastecreative.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.wastecreative.wastecreative.data.database.CraftEntity
import com.wastecreative.wastecreative.data.database.WasteCreativeDB
import com.wastecreative.wastecreative.data.models.Craft
import com.wastecreative.wastecreative.data.models.CraftDetail
import com.wastecreative.wastecreative.data.models.UploadResponse
import com.wastecreative.wastecreative.data.network.ApiService
import com.wastecreative.wastecreative.data.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CraftRepository(
    private val wasteCreativeDB: WasteCreativeDB,
    private val apiService: ApiService,
) {
    fun getCrafts(): LiveData<PagingData<CraftEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = CraftRemoteMediator(wasteCreativeDB, apiService),
            pagingSourceFactory = {
                wasteCreativeDB.craftDao().getAllCrafts()
            }
        ).liveData
    }
    suspend fun refresh() {
        wasteCreativeDB.craftDao().deleteAll()
        wasteCreativeDB.remoteKeysDao().deleteRemoteKeys()

    }
    suspend fun fetchCraftList(): Flow<Result<List<Craft>>> {
        return flow {
            emit(Result.Loading)
            try {
                val result = apiService.getCraftList(4)
                emit(Result.Success(result.data))
            } catch (e: Exception) {
                Log.d("CraftRepository", "fetchCraftList: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun fetchCraftDetail(id: String): Flow<Result<CraftDetail>>{
        return flow {
            emit(Result.Loading)
            try {
                val result = apiService.getCraftDetail(id)
                emit(Result.Success(result.data))
            } catch (e: Exception) {
                Log.d("CraftRepository", "fetchCraftList: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchCraftSearchResult(query: String): Flow<Result<List<Craft>>?> {
        return flow {
            emit(Result.Loading)
            try {
                val result = apiService.getSearcCraftList(query)
                emit(Result.Success(result.data))
            } catch (e: Exception) {
                Log.d("CraftRepository", "fetchCraftSearchResult: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun postCraft(
        file: MultipartBody.Part,
        name: RequestBody,
        materials: ArrayList<String>,
        tools: ArrayList<String>,
        steps: ArrayList<String>,
        video: RequestBody,
        user_id: RequestBody,
    ): Flow<Result<UploadResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val result = apiService.uploadCraft(file, name, materials, tools, steps, video, user_id)
                emit(Result.Success(result))
            } catch (e: Exception) {
                Log.d("StoryRepository", "UploadStory: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
    companion object {
        @Volatile
        private var instance: CraftRepository? = null
        fun getInstance(
            wasteCreativeDB: WasteCreativeDB,
            apiService: ApiService,
        ): CraftRepository =
            instance ?: synchronized(this) {
                instance ?: CraftRepository(wasteCreativeDB, apiService)
            }.also { instance = it }
    }
}