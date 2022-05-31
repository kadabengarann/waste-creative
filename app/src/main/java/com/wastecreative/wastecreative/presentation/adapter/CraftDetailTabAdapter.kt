package com.wastecreative.wastecreative.presentation.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wastecreative.wastecreative.presentation.view.craft.DetailCraftTabFragment

class CraftDetailTabAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity)  {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = DetailCraftTabFragment()
        fragment.arguments = Bundle().apply {
            putInt(DetailCraftTabFragment.ARG_SECTION_NUMBER, position + 1)
        }
        return fragment
    }
}