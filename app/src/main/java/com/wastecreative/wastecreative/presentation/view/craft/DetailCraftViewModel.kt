package com.wastecreative.wastecreative.presentation.view.craft

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wastecreative.wastecreative.data.database.CraftEntity
import com.wastecreative.wastecreative.data.models.Craft
import com.wastecreative.wastecreative.data.models.CraftDetail
import com.wastecreative.wastecreative.data.repositories.CraftRepository
import com.wastecreative.wastecreative.data.network.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class DetailCraftViewModel (private val craftRepository: CraftRepository) : ViewModel() {

    private val _idCraft = MutableLiveData<String>()
    val idCraft: LiveData<String> = _idCraft

    private val _craft = MutableLiveData<Result<CraftDetail>>()
    val craftDetail: LiveData<Result<CraftDetail>> = _craft

    fun setId(id: String) {
        _idCraft.value = id
    }

    fun detailCraft(param: String) {
        viewModelScope.launch {
            craftRepository.fetchCraftDetail(param).collect {
                _craft.postValue(it)
            }
        }
    }
}