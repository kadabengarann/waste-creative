package com.wastecreative.wastecreative.presentation.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wastecreative.wastecreative.data.database.CraftEntity
import com.wastecreative.wastecreative.data.models.Craft
import com.wastecreative.wastecreative.data.models.preference.UserModel
import com.wastecreative.wastecreative.data.models.preference.UserPreferences
import com.wastecreative.wastecreative.data.repositories.CraftRepository
import com.wastecreative.wastecreative.data.network.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class HomeViewModel (private val craftRepository: CraftRepository, private val userPreferences: UserPreferences) : ViewModel() {

    private val _listCraft = MutableLiveData<Result<List<Craft>>>()
    val listCraft: LiveData<Result<List<Craft>>> = _listCraft

    private val _userData = MutableLiveData<UserModel>()
    val userData: LiveData<UserModel> = _userData

    init {
        getCrafts()
        viewModelScope.launch {
            userPreferences.getUser().collect {
                _userData.postValue(it)
            }
        }
    }
    fun refresh() {
        viewModelScope.launch {
            craftRepository.refresh()
        }
    }

    fun getCrafts() {
        viewModelScope.launch {
            craftRepository.fetchCraftList().collect {
                _listCraft.postValue(it)
            }
        }
    }
    fun logOut(){
        viewModelScope.launch {
            userPreferences.logout()
        }
    }
}