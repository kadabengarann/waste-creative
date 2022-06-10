package com.wastecreative.wastecreative.presentation.view.craft

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.data.models.CraftDetail
import com.wastecreative.wastecreative.databinding.ActivityDetailCraftBinding
import com.wastecreative.wastecreative.di.ViewModelFactory
import com.wastecreative.wastecreative.presentation.adapter.CraftDetailTabAdapter
import com.wastecreative.wastecreative.utils.loadImage
import com.wastecreative.wastecreative.data.network.Result


class DetailCraftActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailCraftBinding

    private val sectionsPagerAdapter = CraftDetailTabAdapter(this)

    private var idCraft = ""

    private val factory by lazy {
        ViewModelFactory.getInstance(this)
    }
    private val viewModel: DetailCraftViewModel by viewModels {
        factory
    }
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

//        val dataCraft = intent.getParcelableExtra<CraftEntity>(EXTRA_CRAFT) as CraftEntity
        idCraft = intent.getStringExtra(EXTRA_CRAFT).toString()

        if (viewModel.craftDetail.value == null) {
            getCraftDetail(idCraft)
        }

        observeData()
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeData() {
        viewModel.craftDetail.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        setCraftDetail(result.data)
                        sectionsPagerAdapter.setDetail(result.data)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showError(result.error)
                    }
                }
            }
        }
    }

    private fun setCraftDetail(data: CraftDetail) {
        binding.apply {
            tvUsername.text = data.userName
            imgAvatar.loadImage(data.userPhoto,25)
            imgCraft.loadImage(data.photo,40)
            tvCraftName.text = data.name
            toolbarLayout.title = data.name
        }
    }

    private fun getCraftDetail(user: String) {
        viewModel.detailCraft(user)
    }

    private fun hideAll(bool: Boolean) {
        if (bool) {
            binding.apply {
                contentHeading.visibility = View.GONE
                viewPager.visibility = View.GONE
            }
        } else {
            binding.apply {
                contentHeading.visibility = View.VISIBLE
                viewPager.visibility = View.VISIBLE
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(msg: String) {
        hideAll(true)
        binding.grError.visibility = View.VISIBLE
        binding.tvError.text = msg
        binding.btnError.setOnClickListener {
            getCraftDetail(idCraft)
            hideAll(false)
            binding.grError.visibility = View.GONE
        }
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