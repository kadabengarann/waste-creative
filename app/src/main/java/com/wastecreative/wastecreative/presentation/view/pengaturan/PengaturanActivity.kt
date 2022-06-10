package com.wastecreative.wastecreative.presentation.view.pengaturan


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.wastecreative.wastecreative.ViewModelFactory
import com.wastecreative.wastecreative.data.models.model.preference.UserPreferences
import com.wastecreative.wastecreative.databinding.ActivityPengaturanBinding
import com.wastecreative.wastecreative.presentation.view.viewModel.MainViewModel
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class PengaturanActivity : AppCompatActivity() {
    private lateinit var _binding : ActivityPengaturanBinding
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPengaturanBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        setupViewModel()
        setupAction()
    }
    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[MainViewModel::class.java]
//
    }

    private fun setupAction() {
        _binding.logoutButton.setOnClickListener {
            mainViewModel.exit()

        }
    }
}