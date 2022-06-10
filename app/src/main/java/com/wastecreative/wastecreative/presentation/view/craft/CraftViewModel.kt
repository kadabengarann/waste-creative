package com.wastecreative.wastecreative.presentation.view.craft

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

class CraftViewModel (private val craftRepository: CraftRepository) : ViewModel() {

    private val _listCraft = MutableLiveData<Result<List<Craft>>>()
    val listCraft: LiveData<Result<List<Craft>>> = _listCraft

    fun refresh() {
        viewModelScope.launch {
            craftRepository.refresh()
        }
    }
    fun getCrafts(): LiveData<PagingData<CraftEntity>> =
        craftRepository.getCrafts().cachedIn(viewModelScope)

    fun getStories() {
        viewModelScope.launch {
            try {

            }catch (exception: Exception){

            }
            craftRepository.fetchCraftList().collect {
                _listCraft.postValue(it)
            }
        }
    }
}