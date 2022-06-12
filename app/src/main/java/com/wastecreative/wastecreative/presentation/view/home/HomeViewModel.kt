package com.wastecreative.wastecreative.presentation.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wastecreative.wastecreative.data.database.CraftEntity
import com.wastecreative.wastecreative.data.models.Craft
import com.wastecreative.wastecreative.data.repositories.CraftRepository
import com.wastecreative.wastecreative.data.network.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class HomeViewModel (private val craftRepository: CraftRepository) : ViewModel() {

    private val _listCraft = MutableLiveData<Result<List<Craft>>>()
    val listCraft: LiveData<Result<List<Craft>>> = _listCraft

    init {
        getCrafts()
    }
    fun refresh() {
        viewModelScope.launch {
            craftRepository.refresh()
        }
    }

    private fun getCrafts() {
        viewModelScope.launch {
            craftRepository.fetchCraftList().collect {
                _listCraft.postValue(it)
            }
        }
    }
}