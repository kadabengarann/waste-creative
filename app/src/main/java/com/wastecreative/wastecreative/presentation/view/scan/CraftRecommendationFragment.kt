package com.wastecreative.wastecreative.presentation.view.scan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.wastecreative.wastecreative.data.models.Craft
import com.wastecreative.wastecreative.data.network.Result
import com.wastecreative.wastecreative.databinding.FragmentCraftRecommendationBinding
import com.wastecreative.wastecreative.di.ViewModelFactory
import com.wastecreative.wastecreative.presentation.adapter.CraftsListAdapter
import com.wastecreative.wastecreative.presentation.view.craft.DetailCraftActivity
import com.wastecreative.wastecreative.utils.getColorFromAttr

class CraftRecommendationFragment : Fragment() {
    private var binding: FragmentCraftRecommendationBinding? = null
    private val craftListAdapter: CraftsListAdapter by lazy {
        CraftsListAdapter(
            arrayListOf()
        )
    }
    private val factory by lazy {
        ViewModelFactory.getInstance(requireActivity())
    }
    private val viewModel: CraftRecommendViewModel by viewModels {
        factory
    }
    private val data = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCraftRecommendationBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.apply {
            setSupportActionBar(binding?.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        requireActivity().window.statusBarColor = requireContext().getColorFromAttr(com.google.android.material.R.attr.colorSecondary)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listObj = CraftRecommendationFragmentArgs.fromBundle(arguments as Bundle).objList
        data.addAll(listObj.toList())
        binding?.rvCrafts?.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = craftListAdapter
        }
        setupViewModel()
        observeData()
    }

    private fun setupViewModel() {
        viewModel.recomCraft(data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun observeData() {
        viewModel.listSearchResult.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val craftList = result.data
                        setSearchCraftData(craftList)
                    }
                    is Result.Error -> {
                        showLoading(false)

                        showError(result.error)
                    }
                }
            }
        }
    }

    private fun setSearchCraftData(craftList: List<Craft>) {
        val listCraft = ArrayList<Craft>()
        if (craftList.isEmpty()) {
            hideAll()
            binding?.grNoSearchResult?.visibility = View.VISIBLE
        } else {
            for (craft in craftList) {
                listCraft.add(craft)
            }
            craftListAdapter.setData(craftList)
            craftListAdapter.setOnItemClickCallback(object : CraftsListAdapter.OnItemClickCallback {
                override fun onItemClicked(data: String) {
                    val intentToDetail = Intent(requireActivity(), DetailCraftActivity::class.java)
                    intentToDetail.putExtra(DetailCraftActivity.EXTRA_CRAFT, data)
                    startActivity(intentToDetail)
                }
            })
        }
    }
    private fun hideAll() {
        binding?.rvCrafts?.visibility = View.GONE
        binding?.progressBar?.visibility = View.GONE
        binding?.grNoSearchResult?.visibility = View.GONE
        binding?.grError?.visibility = View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(msg: String) {
        Toast.makeText(
            requireContext(),
            "Gagal mengambil data",
            Toast.LENGTH_SHORT
        ).show()
        hideAll()
        binding?.grError?.visibility = View.VISIBLE
        binding?.tvError?.text = msg
        binding?.btnError?.setOnClickListener {
                viewModel.recomCraft(data)
            binding?.grError?.visibility = View.GONE
        }
    }
}