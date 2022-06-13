package com.wastecreative.wastecreative.presentation.view.marketplace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wastecreative.wastecreative.data.database.CraftEntity
import com.wastecreative.wastecreative.data.database.MarketplaceEntity
import com.wastecreative.wastecreative.data.models.Marketplace
import com.wastecreative.wastecreative.data.repositories.CraftRepository
import com.wastecreative.wastecreative.data.network.Result
import com.wastecreative.wastecreative.data.repositories.MarketplaceRepository
import kotlinx.coroutines.launch

class MarketplaceViewModel (private val marketplaceRepository: MarketplaceRepository) : ViewModel() {

    private val _listPost = MutableLiveData<Result<List<Marketplace>>>()
    val listMarketplace: LiveData<Result<List<Marketplace>>> = _listPost

    fun refresh() {
        viewModelScope.launch {
            marketplaceRepository.refresh()
        }
    }
    fun getPosts(): LiveData<PagingData<MarketplaceEntity>> =
        marketplaceRepository.getPosts().cachedIn(viewModelScope)

}