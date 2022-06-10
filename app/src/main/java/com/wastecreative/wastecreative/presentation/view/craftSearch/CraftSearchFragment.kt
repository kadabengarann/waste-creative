package com.wastecreative.wastecreative.presentation.view.craftSearch

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.data.models.Craft
import com.wastecreative.wastecreative.databinding.FragmentCraftSearchBinding
import com.wastecreative.wastecreative.di.ViewModelFactory
import com.wastecreative.wastecreative.data.network.Result
import com.wastecreative.wastecreative.presentation.adapter.CraftsListAdapter
import com.wastecreative.wastecreative.presentation.view.craft.DetailCraftActivity

class CraftSearchFragment : Fragment() {

    private var _binding: FragmentCraftSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchView: SearchView
    private var orientChanged: Boolean = false

    private val factory by lazy {
        ViewModelFactory.getInstance(requireActivity())
    }
    private val viewModel: CraftSearchViewModel by viewModels {
        factory
    }
    private val craftListAdapter: CraftsListAdapter by lazy {
        CraftsListAdapter(
            ArrayList()
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCraftSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setHasOptionsMenu(true)
        }
        observeData()

        binding.rvCrafts.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = craftListAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, menuInflater)
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchMenu = menu.findItem(R.id.search)
        searchView = searchMenu.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.queryHint = "Enter craft name"

        val tempQuery = viewModel.queryValue.value
        searchMenu.expandActionView()

        if (tempQuery != null && tempQuery.isNotEmpty()) {
            searchView.setQuery(tempQuery, false)
        } else {
            binding.grNoQuery.visibility = View.VISIBLE
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNullOrBlank()) {
                    hideAll()
                    binding.grNoQuery.visibility = View.VISIBLE
                } else {
                    hideAll()
                    binding.rvCrafts.visibility = View.VISIBLE
                    viewModel.searchCraft(query)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (query.isNullOrBlank()) {
                    hideAll()
                    binding.grNoQuery.visibility = View.VISIBLE
                } else {
                    viewModel.setQuery(query)
                    hideAll()
                    binding.rvCrafts.visibility = View.VISIBLE
                    viewModel.searchCraft(query)
                }
                return false
            }
        })
        searchView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View?) {}

            override fun onViewDetachedFromWindow(p0: View?) {
                if (!orientChanged) {
                    view?.findNavController()?.navigateUp()
                }
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
        orientChanged = true
        _binding = null
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
            binding.grNoSearchResult.visibility = View.VISIBLE
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
        binding.rvCrafts.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.grNoSearchResult.visibility = View.GONE
        binding.grNoQuery.visibility = View.GONE
        binding.grError.visibility = View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(msg: String) {
        Toast.makeText(
            requireContext(),
            "Gagal mengambil gambar.",
            Toast.LENGTH_SHORT
        ).show()
        val tmpQuery = viewModel.queryValue.value
        hideAll()
        binding.grError.visibility = View.VISIBLE
        binding.tvError.text = msg
        binding.btnError.setOnClickListener {
            if (tmpQuery != null) {
                viewModel.searchCraft(tmpQuery)
            }
            binding.grError.visibility = View.GONE
        }
    }
}