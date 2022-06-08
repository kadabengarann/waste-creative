package com.wastecreative.wastecreative.presentation.view.craft

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.data.models.model.Craft
import com.wastecreative.wastecreative.databinding.ActivityDetailCraftBinding
import com.wastecreative.wastecreative.presentation.adapter.CraftDetailTabAdapter
import com.wastecreative.wastecreative.utils.loadImage

class DetailCraftActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailCraftBinding

    private val sectionsPagerAdapter = CraftDetailTabAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailCraftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        viewPager.offscreenPageLimit = 2 / 2
        val tabs: TabLayout = binding.tabsCraftDetail
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        val dataCraft = intent.getParcelableExtra<Craft>(EXTRA_CRAFT) as Craft

        binding.apply {
            tvUsername.text = "Saifuddin"
            imgAvatar.loadImage(dataCraft.userPhoto,25)
            imgCraft.loadImage(dataCraft.photo,40)
            tvCraftName.text = dataCraft.name
            toolbarLayout.title = dataCraft.name
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_tools_and_material_text,
            R.string.tab_steps_text,
        )
        const val EXTRA_CRAFT = "extra_object"
    }
}