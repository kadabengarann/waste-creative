package com.wastecreative.wastecreative.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.wastecreative.wastecreative.data.database.CraftEntity
import com.wastecreative.wastecreative.data.database.WasteCreativeDB
import com.wastecreative.wastecreative.data.models.Craft
import com.wastecreative.wastecreative.data.models.CraftDetail
import com.wastecreative.wastecreative.data.network.ApiService
import com.wastecreative.wastecreative.data.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
                val result = apiService.getCraftList()
                emit(Result.Success(result))
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
                emit(Result.Success(result))
            } catch (e: Exception) {
                Log.d("CraftRepository", "fetchCraftList: ${e.message.toString()} ")
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