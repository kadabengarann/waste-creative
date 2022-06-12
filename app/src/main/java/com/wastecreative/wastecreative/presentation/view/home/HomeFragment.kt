package com.wastecreative.wastecreative.presentation.view.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.data.models.Craft
import com.wastecreative.wastecreative.data.network.Result
import com.wastecreative.wastecreative.databinding.FragmentHomeBinding
import com.wastecreative.wastecreative.di.ViewModelFactory
import com.wastecreative.wastecreative.presentation.adapter.CraftsListAdapter
import com.wastecreative.wastecreative.presentation.adapter.PagingCraftsListAdapter
import com.wastecreative.wastecreative.presentation.view.craft.CraftViewModel
import com.wastecreative.wastecreative.presentation.view.craft.DetailCraftActivity
import com.wastecreative.wastecreative.presentation.view.scan.ScanActivity
import com.wastecreative.wastecreative.utils.loadImage
import de.hdodenhof.circleimageview.CircleImageView


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private val craftListAdapter: CraftsListAdapter by lazy {
        CraftsListAdapter(
            arrayListOf()
        )
    }
    private val factory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }
    private val viewModel: HomeViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
        }
        setHasOptionsMenu(true)
        binding.contentHome.rvCrafts.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = craftListAdapter
        }

        observeData()
        showRecyclerList()
        setupAction()
    }

    private fun observeData() {
        viewModel.listCraft.observe(viewLifecycleOwner){result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
//                        showLoading(true)
                    }
                    is Result.Success -> {
//                        showLoading(false)
                        setContent(result.data)
                    }
                    is Result.Error -> {
//                        showLoading(false)
//                        showError()
                    }
                }
            }
        }
    }

    private fun setContent(data: List<Craft>) {
        craftListAdapter.setData(data)
        craftListAdapter.setOnItemClickCallback(object : CraftsListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                val intentToDetail = Intent(requireActivity(), DetailCraftActivity::class.java)
                intentToDetail.putExtra(DetailCraftActivity.EXTRA_CRAFT, data)
                startActivity(intentToDetail)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupAction() {
        binding.apply {
            ctaHome.setOnClickListener{
                view?.findNavController()?.navigate(R.id.action_navigation_home_to_navigation_craft_search)
            }
            contentHome.tvShowAll.setOnClickListener {
                view?.findNavController()?.navigate(R.id.action_navigation_home_to_navigation_craft)
            }
        }
    }

    private fun showRecyclerList() {

    }
    override fun onCreateOptionsMenu(menu: Menu , inflater: MenuInflater){
        menu.clear()
        super.onCreateOptionsMenu(menu,inflater)
        inflater.inflate(R.menu.home_menu, menu)

        val profileMenu = menu.findItem(R.id.menu_two)
        val layoutProfileMenu = profileMenu.actionView as FrameLayout
        val avatarImg = layoutProfileMenu.findViewById(R.id.toolbar_profile_image) as CircleImageView
        avatarImg.loadImage("https://picsum.photos/300/300?random=69") // Change to user Avatar
        avatarImg.setOnClickListener {
            val intent =Intent(requireContext(), ScanActivity::class.java)
            startActivity(intent)
            Toast.makeText(requireContext(), getString(R.string.title_home), Toast.LENGTH_SHORT).show()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting ->
                // Not implemented here
                return false
            else -> {}
        }
        return false
    }

}