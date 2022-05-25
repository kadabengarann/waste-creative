package com.wastecreative.wastecreative.presentation.view.profile

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.adapter.SectionProfilAdapter


import com.wastecreative.wastecreative.databinding.ActivityProfileBinding



class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionProfilAdapter = SectionProfilAdapter(this)
        val viewPager:ViewPager2 = binding.viewPagerDetailProfile
        viewPager.adapter = sectionProfilAdapter
        viewPager.offscreenPageLimit = 2 / 2
        val tabs: TabLayout = binding.tabLayout

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }
}


