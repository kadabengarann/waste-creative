package com.wastecreative.wastecreative.presentation.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wastecreative.wastecreative.data.models.model.preference.UserModel
import com.wastecreative.wastecreative.data.models.model.preference.UserPreferences

class MainViewModel(private val pref: UserPreferences) : ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

//    fun exit() {
//        viewModelScope.launch {
//            pref.logout()
//        }
//    }

}