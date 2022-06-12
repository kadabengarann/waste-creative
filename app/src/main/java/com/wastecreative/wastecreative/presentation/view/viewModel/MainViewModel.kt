package com.wastecreative.wastecreative.presentation.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wastecreative.wastecreative.data.models.ResponseItem
import com.wastecreative.wastecreative.data.models.preference.UserModel
import com.wastecreative.wastecreative.data.models.preference.UserPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserPreferences) : ViewModel() {
    fun getUser(): LiveData<ResponseItem> {
        return pref.getUser().asLiveData()
    }

    fun exit() {
        viewModelScope.launch {
            pref.logout()
        }
    }

}