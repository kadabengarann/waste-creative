package com.wastecreative.wastecreative.presentation.view.craft

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.data.database.CraftEntity
import com.wastecreative.wastecreative.databinding.FragmentCraftBinding
import com.wastecreative.wastecreative.di.ViewModelFactory
import com.wastecreative.wastecreative.presentation.adapter.LoadingStateAdapter
import com.wastecreative.wastecreative.presentation.adapter.PagingCraftsListAdapter


class CraftFragment : Fragment() {
    private var _binding: FragmentCraftBinding? = null
    private val binding get() = _binding!!

    private val craftListAdapter: PagingCraftsListAdapter by lazy {
        PagingCraftsListAdapter()
    }
    private val factory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }
    private val craftViewModel: CraftViewModel by viewModels {
        factory
    }
    private var reFetch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCraftBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        setupAction()
        setupViewModel()
        binding.rvCrafts.apply {
            val layoutManager = GridLayoutManager(context, 2)
            this.layoutManager = layoutManager
            setHasFixedSize(true)
            val footerAdapter = LoadingStateAdapter {
                craftListAdapter.retry()
            }
            val listAdapter = craftListAdapter.withLoadStateFooter(
                footer = footerAdapter
            )
            adapter = listAdapter
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == listAdapter.itemCount - 1 && footerAdapter.itemCount > 0){
                        // if it is the last position and we have a footer
                        2
                    } else {
                        1
                    }
                }
            }
        }
        observeData()
    }

    private fun setupViewModel() {
        if (reFetch) {
            craftViewModel.refresh()
            reFetch = false
        }
        craftViewModel.getCrafts().observe(viewLifecycleOwner) {
            craftListAdapter.submitData(lifecycle, it)
            binding.rvCrafts.visibility = View.VISIBLE
        }
        binding.rvCrafts.scrollToPosition(0)
        showLoading(true)

    }

    private fun observeData() {
        craftListAdapter.addLoadStateListener { loadState ->
            showLoading(loadState.source.refresh is LoadState.Loading)
            binding.grError.isVisible = loadState.source.refresh is LoadState.Error
            showError()
        }
        craftListAdapter.setOnItemClickCallback(object : PagingCraftsListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                val intentToDetail = Intent(requireActivity(), DetailCraftActivity::class.java)
                intentToDetail.putExtra(DetailCraftActivity.EXTRA_CRAFT, data)
                startActivity(intentToDetail)
            }
        })

    }

    private fun setupAction() {
        binding.fabAddCraft.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_navigation_craft_to_navigation_add_detail_craft)
        }
    }
    private fun String.insert(insertAt: Int, string: String): String {
        return this.substring(0, insertAt) + string + this.substring(insertAt, this.length)
    }
    private fun showError() {
        binding.btnError.setOnClickListener {
            binding.grError.visibility = View.GONE
            craftListAdapter.retry()
        }
    }

    private fun showLoading(b: Boolean) {
        binding.progressBar.visibility = if (b) View.VISIBLE else View.GONE
    }
}