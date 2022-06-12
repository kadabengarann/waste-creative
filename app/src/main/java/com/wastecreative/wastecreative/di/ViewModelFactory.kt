package com.wastecreative.wastecreative.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wastecreative.wastecreative.data.models.preference.UserPreferences
import com.wastecreative.wastecreative.data.repositories.CraftRepository
import com.wastecreative.wastecreative.data.repositories.MarketplaceRepository
import com.wastecreative.wastecreative.presentation.view.addMarketplace.AddMarketplaceViewModel
import com.wastecreative.wastecreative.presentation.view.addcraft.AddCraftViewModel
import com.wastecreative.wastecreative.presentation.view.craft.CraftViewModel
import com.wastecreative.wastecreative.presentation.view.craft.DetailCraftViewModel
import com.wastecreative.wastecreative.presentation.view.craftSearch.CraftSearchViewModel
import com.wastecreative.wastecreative.presentation.view.detailMarketplace.DetailMarketplaceViewModel
import com.wastecreative.wastecreative.presentation.view.home.HomeViewModel
import com.wastecreative.wastecreative.presentation.view.marketplace.MarketplaceViewModel
import com.wastecreative.wastecreative.presentation.view.viewModel.LoginViewModel

class ViewModelFactory(private val craftRepository: CraftRepository, private val marketplaceRepository: MarketplaceRepository, private val pref: UserPreferences) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        CraftViewModel::class.java -> CraftViewModel(craftRepository)
        DetailCraftViewModel::class.java -> DetailCraftViewModel(craftRepository)
        CraftSearchViewModel::class.java -> CraftSearchViewModel(craftRepository)
        HomeViewModel::class.java -> HomeViewModel(craftRepository)
        AddCraftViewModel::class.java -> AddCraftViewModel(craftRepository,pref)
        MarketplaceViewModel::class.java -> MarketplaceViewModel(marketplaceRepository)
        DetailMarketplaceViewModel::class.java -> DetailMarketplaceViewModel(marketplaceRepository, pref)
        AddMarketplaceViewModel::class.java -> AddMarketplaceViewModel(marketplaceRepository,pref)
        LoginViewModel::class.java -> LoginViewModel(pref)
        else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    } as T

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideCraftRepository(context),
                    Injection.provideMarketplaceRepository(context),
                    Injection.provideUserPreference(context))
            }.also { instance = it }
    }
}