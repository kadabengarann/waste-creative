package com.wastecreative.wastecreative.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.ViewModelFactory
import com.wastecreative.wastecreative.data.models.preference.UserPreferences
import com.wastecreative.wastecreative.databinding.ActivityMainBinding
import com.wastecreative.wastecreative.presentation.view.boarding.BoardingActivity
import com.wastecreative.wastecreative.presentation.view.scan.ScanActivity
import com.wastecreative.wastecreative.presentation.view.viewModel.MainViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomAppBar: BottomAppBar
    private lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setupBottomNavigation()
        setupAction()
        setupViewModel()

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun setupAction() {
        binding.scanFab.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }
    }
    private fun setupBottomNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
        bottomAppBar = binding.bottomAppBar

        val navView: BottomNavigationView = binding.navView

        navView.setupWithNavController(navController)

        navView.background = null
        navView.menu.getItem(1).isEnabled = false

        initDestinationListener()

    }

    private fun initDestinationListener() {

        navController.addOnDestinationChangedListener { _, destination: NavDestination, _->
            when (destination.id) {
                R.id.navigation_home -> showBottomNav(true)
                R.id.navigation_marketplace -> showBottomNav(true)
                else -> showBottomNav(false)
            }
        }
    }

    private fun showBottomNav(bool: Boolean) {
        val height = bottomAppBar.height
        (binding.navHostFragmentActivityMain.layoutParams as ViewGroup.MarginLayoutParams).apply {
            if (bool)
                setMargins(0, 0, 0, height)
            else
                setMargins(0, 0, 0, 0)
        }
        bottomAppBar.apply {
            if (bool) performShow() else performHide()
        }
        binding.scanFab.visibility = if (bool) View.VISIBLE else View.GONE
        binding.bottomAppBar.visibility = if (bool) View.VISIBLE else View.GONE
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(this))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this, { user ->
            if (user.isLogin){
                Log.d("cek","{$user}")

            } else {
                startActivity(Intent(this, BoardingActivity::class.java))
                finish()
            }
        })
    }
}