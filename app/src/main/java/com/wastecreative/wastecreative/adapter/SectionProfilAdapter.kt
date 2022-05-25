package com.wastecreative.wastecreative.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wastecreative.wastecreative.presentation.view.profile.FragmentMarket
import com.wastecreative.wastecreative.presentation.view.profile.KerajinanFragment

class SectionProfilAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = KerajinanFragment()
            1 -> fragment = FragmentMarket()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}