package com.wastecreative.wastecreative.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wastecreative.wastecreative.data.repositories.CraftRepository
import com.wastecreative.wastecreative.presentation.view.craft.CraftViewModel
import com.wastecreative.wastecreative.presentation.view.craft.DetailCraftViewModel
import com.wastecreative.wastecreative.presentation.view.craftSearch.CraftSearchViewModel

class ViewModelFactory(private val craftRepository: CraftRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        CraftViewModel::class.java -> CraftViewModel(craftRepository)
        DetailCraftViewModel::class.java -> DetailCraftViewModel(craftRepository)
        CraftSearchViewModel::class.java -> CraftSearchViewModel(craftRepository)
        else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    } as T

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideCraftRepository(context))
            }.also { instance = it }
    }
}