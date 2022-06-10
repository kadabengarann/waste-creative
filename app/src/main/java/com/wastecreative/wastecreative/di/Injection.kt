package com.wastecreative.wastecreative.di

import android.content.Context
import com.wastecreative.wastecreative.data.database.WasteCreativeDB
import com.wastecreative.wastecreative.data.network.ApiConfig
import com.wastecreative.wastecreative.data.repositories.CraftRepository


object Injection {
    fun provideCraftRepository(context: Context): CraftRepository {
        val apiService = ApiConfig.getApiService()
        val wasteCreativeDB = WasteCreativeDB.getDatabase(context)
        return CraftRepository.getInstance(wasteCreativeDB, apiService)
    }
}