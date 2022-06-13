package com.wastecreative.wastecreative.presentation.view.profile

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.ViewModelFactory
import com.wastecreative.wastecreative.data.models.preference.UserPreferences
import com.wastecreative.wastecreative.presentation.adapter.SectionProfilAdapter


import com.wastecreative.wastecreative.databinding.ActivityProfileBinding
import com.wastecreative.wastecreative.presentation.view.detailMarketplace.DetailMarketplaceActivity
import com.wastecreative.wastecreative.presentation.view.viewModel.MainViewModel
import com.wastecreative.wastecreative.utils.loadImage


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var mainViewModel: MainViewModel

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        const val EXTRA_IMG = "extra_object"
        const val EXTRA_UNAME = "extra_object"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var array = intent.getStringArrayExtra(EXTRA_IMG)
//        var uname = intent.getStringExtra(EXTRA_UNAME).toString()

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Profile"

//        val sectionProfilAdapter = SectionProfilAdapter(this)
//        val viewPager:ViewPager2 = binding.viewPagerDetailProfile
//        viewPager.adapter = sectionProfilAdapter
//        viewPager.offscreenPageLimit = 2 / 2
//        val tabs: TabLayout = binding.tabLayout

//        TabLayoutMediator(tabs, viewPager) { tab, position ->
//            tab.text = resources.getString(TAB_TITLES[position])
//        }.attach()
        setupViewModel()
        binding.imageProfile.loadImage(array?.get(0), 100)
        binding.tvNamaUserDetail.text = array?.get(1)
        binding.signupButton.setOnClickListener {
            mainViewModel.exit()
            finish()
        }
    }
    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(this))
        )[MainViewModel::class.java]
//
    }
}


