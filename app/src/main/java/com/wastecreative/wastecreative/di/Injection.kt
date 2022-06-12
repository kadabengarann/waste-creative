package com.wastecreative.wastecreative.di

import android.content.Context
import com.wastecreative.wastecreative.data.database.WasteCreativeDB
import com.wastecreative.wastecreative.data.models.preference.UserPreferences
import com.wastecreative.wastecreative.data.network.ApiConfig
import com.wastecreative.wastecreative.data.repositories.CraftRepository
import com.wastecreative.wastecreative.data.repositories.MarketplaceRepository

object Injection {
    fun provideCraftRepository(context: Context): CraftRepository {
        val apiService = ApiConfig.getApiService()
        val wasteCreativeDB = WasteCreativeDB.getDatabase(context)
        return CraftRepository.getInstance(wasteCreativeDB, apiService)
    }

    fun provideMarketplaceRepository(context: Context): MarketplaceRepository {
        val apiService = ApiConfig.getApiService()
        val wasteCreativeDB = WasteCreativeDB.getDatabase(context)
        return MarketplaceRepository.getInstance(wasteCreativeDB, apiService)
    }
    fun provideUserPreference(context: Context): UserPreferences {
        return UserPreferences.getInstance(context)
    }
}