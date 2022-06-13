package com.wastecreative.wastecreative.presentation.view.detailMarketplace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.google.android.material.tabs.TabLayoutMediator
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.data.models.MarketplaceDetail
import com.wastecreative.wastecreative.data.network.Result
import com.wastecreative.wastecreative.databinding.ActivityDetailMarketplaceBinding
import com.wastecreative.wastecreative.di.ViewModelFactory
import com.wastecreative.wastecreative.presentation.adapter.TabPageAdapter
import com.wastecreative.wastecreative.utils.loadImage
import okhttp3.internal.immutableListOf

class DetailMarketplaceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMarketplaceBinding

    private val factory by lazy {
        ViewModelFactory.getInstance(this)
    }
    private val viewModel: DetailMarketplaceViewModel by viewModels {
        factory
    }

    private var idPost : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMarketplaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutAdapter = TabPageAdapter(
            immutableListOf(PostDetailFragment(), PostCommentFragment()),
            supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = layoutAdapter
        binding.viewPager.isUserInputEnabled = false

        TabLayoutMediator(binding.tabsCraftDetail, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        idPost = intent.getStringExtra(DetailMarketplaceActivity.EXTRA_POST).toString().toInt()

        if (viewModel.marketplaceDetail.value == null) {
            idPost?.let {
                getMarketplaceDetail(it)
            }
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
        viewModel.marketplaceDetail.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        setMarketplaceDetail(result.data)
//                        sectionsPagerAdapter.setDetail(result.data)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showError(result.error)
                    }
                }
            }
        }
    }

    private fun setMarketplaceDetail(data: MarketplaceDetail) {
        binding.apply {
            tvUsername.text = data.userName
            imgAvatar.loadImage(data.userPhoto,25)
            imgCraft.loadImage(data.foto,40)
            tvCraftName.text = data.title
            toolbarLayout.title = data.title
        }
    }

    private fun getMarketplaceDetail(user: Int) {
        viewModel.detailMarketplace(user)
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
            idPost?.let {
                getMarketplaceDetail(it)
            }
            hideAll(false)
            binding.grError.visibility = View.GONE
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.description,
            R.string.comments,
        )

        const val EXTRA_POST = "extra_object"

    }
}