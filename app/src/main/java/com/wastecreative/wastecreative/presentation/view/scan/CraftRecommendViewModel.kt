package com.wastecreative.wastecreative.presentation.view.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wastecreative.wastecreative.data.models.Craft
import com.wastecreative.wastecreative.data.repositories.CraftRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.wastecreative.wastecreative.data.network.Result

class CraftRecommendViewModel(private val craftRepository: CraftRepository) : ViewModel() {
    private val _listSearchResult = MutableLiveData<Result<List<Craft>>>()
    val listSearchResult: LiveData<Result<List<Craft>>> = _listSearchResult

    private val _queryValue = MutableLiveData<String>()
    val queryValue: LiveData<String> = _queryValue

    fun setQuery(query: String) {
        _queryValue.value = query
    }

    fun recomCraft(query: ArrayList<String>) {
        viewModelScope.launch {
            craftRepository.fetchCraftRecommendation(query).collect {
                _listSearchResult.postValue(it)
            }
        }
    }
}