package com.wastecreative.wastecreative.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.databinding.ActivityMainBinding
import com.wastecreative.wastecreative.presentation.view.boarding.BoardingFragment
import com.wastecreative.wastecreative.presentation.view.login.LoginFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomAppBar: BottomAppBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()




    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }


    private fun setupBottomNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
        bottomAppBar = binding.bottomAppBar

        val navView: BottomNavigationView = binding.navView
        val appBarConfiguration = AppBarConfiguration(
            setOf(
               R.id.navigation_home,R.id.navigation_marketplace
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

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
}