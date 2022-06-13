package com.wastecreative.wastecreative.data.repositories

import android.util.Log
import com.wastecreative.wastecreative.data.database.WasteCreativeDB
import com.wastecreative.wastecreative.data.models.CraftDetail
import com.wastecreative.wastecreative.data.models.UploadResponse
import com.wastecreative.wastecreative.data.models.postUserResponse
import com.wastecreative.wastecreative.data.models.userResponse
import com.wastecreative.wastecreative.data.network.ApiService
import com.wastecreative.wastecreative.data.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository(
    private val apiService: ApiService,
) {
    suspend fun fetctUserLoginData(
        email: String,
        password: String
    ): Flow<Result<userResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val result = apiService.fetchLogin(email, password)
                emit(Result.Success(result.data))
            } catch (e: Exception) {
                Log.d("CraftRepository", "fetchCraftList: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postUserData(
        name: String,
        email: String,
        password: String,
        avatar: String,
    ): Flow<Result<postUserResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val result = apiService.postUser(name, email, password, avatar)
                emit(Result.Success(result))
            } catch (e: Exception) {
                Log.d("StoryRepository", "UploadStory: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}