package com.wastecreative.wastecreative.presentation.view.marketplace

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.data.models.Post
import com.wastecreative.wastecreative.databinding.FragmentMarketplaceBinding
import com.wastecreative.wastecreative.di.ViewModelFactory
import com.wastecreative.wastecreative.presentation.adapter.LoadingStateAdapter
import com.wastecreative.wastecreative.presentation.adapter.PagingCraftsListAdapter
import com.wastecreative.wastecreative.presentation.adapter.PagingMarketplaceListAdapter
import com.wastecreative.wastecreative.presentation.adapter.PostListAdapter
import com.wastecreative.wastecreative.presentation.view.craft.CraftFragmentArgs
import com.wastecreative.wastecreative.presentation.view.craft.CraftViewModel
import com.wastecreative.wastecreative.presentation.view.craft.DetailCraftActivity
import com.wastecreative.wastecreative.presentation.view.detailMarketplace.DetailMarketplaceActivity

class MarketplaceFragment : Fragment() {
    private var _binding: FragmentMarketplaceBinding? = null
    private val binding get() = _binding!!

    private val postListAdapter: PagingMarketplaceListAdapter by lazy {
        PagingMarketplaceListAdapter()
    }
    private val factory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }
    private val viewModel: MarketplaceViewModel by viewModels {
        factory
    }
    private var reFetch = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarketplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reFetch = MarketplaceFragmentArgs.fromBundle(arguments as Bundle).reFetch
        setupViewModel()
        setupAction()
        binding.rvCrafts.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = postListAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    postListAdapter.retry()
                }
            )
        }
        observeData()
    }
    private fun setupViewModel() {
        if (reFetch) {
            viewModel.refresh()
            reFetch = false
        }
        viewModel.getPosts().observe(viewLifecycleOwner) {
            postListAdapter.submitData(lifecycle, it)
            binding.rvCrafts.visibility = View.VISIBLE
        }
        binding.rvCrafts.scrollToPosition(0)
        showLoading(true)

    }
    private fun observeData() {
        postListAdapter.addLoadStateListener { loadState ->
            showLoading(loadState.source.refresh is LoadState.Loading)
            binding.grError.isVisible = loadState.source.refresh is LoadState.Error
            showError()
        }
        postListAdapter.setOnItemClickCallback(object : PagingMarketplaceListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                val intentToDetail = Intent(requireActivity(), DetailMarketplaceActivity::class.java)
                intentToDetail.putExtra(DetailMarketplaceActivity.EXTRA_POST, data)
                startActivity(intentToDetail)
            }
        })

    }
    private fun setupAction() {
        binding.fabCreatePost.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_navigation_marketplace_to_navigation_add_marketplace)
        }
    }

    private fun showError() {
        binding.btnError.setOnClickListener {
            binding.grError.visibility = View.GONE
            postListAdapter.retry()
        }
    }

    private fun showLoading(b: Boolean) {
        binding.progressBar.visibility = if (b) View.VISIBLE else View.GONE
    }

}