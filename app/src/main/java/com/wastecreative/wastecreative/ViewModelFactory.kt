package com.wastecreative.wastecreative

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wastecreative.wastecreative.data.models.model.preference.UserPreferences
import com.wastecreative.wastecreative.presentation.view.viewModel.LoginViewModel
import com.wastecreative.wastecreative.presentation.view.viewModel.RegisterViewModel

class ViewModelFactory(private val pref: UserPreferences) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}

