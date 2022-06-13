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
import com.wastecreative.wastecreative.presentation.view.MainActivity
import com.wastecreative.wastecreative.presentation.view.craft.CraftViewModel
import com.wastecreative.wastecreative.presentation.view.craft.DetailCraftActivity
import com.wastecreative.wastecreative.presentation.view.detailMarketplace.DetailMarketplaceActivity
import com.wastecreative.wastecreative.presentation.view.profile.ProfileActivity
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

    private var avatar= ""
    private var UName= ""
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
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        setContent(result.data)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showError(result.error)
                    }
                }
            }
        }
        viewModel.userData.observe(viewLifecycleOwner){
            avatar = it.avatar
            UName = it.name
        }

    }

    private fun setContent(data: List<Craft>) {
        if (data.isEmpty()) {
            hideAll()
            binding.contentHome.grNoSearchResult.visibility = View.VISIBLE
        } else {
            craftListAdapter.setData(data)
            craftListAdapter.setOnItemClickCallback(object : CraftsListAdapter.OnItemClickCallback {
                override fun onItemClicked(data: String) {
                    val intentToDetail = Intent(requireActivity(), DetailCraftActivity::class.java)
                    intentToDetail.putExtra(DetailCraftActivity.EXTRA_CRAFT, data)
                    startActivity(intentToDetail)
                }
            })
        }
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
        avatarImg.setOnClickListener {
            var intent =Intent(requireActivity(), ProfileActivity::class.java)
            intent.putExtra(ProfileActivity.EXTRA_IMG, arrayOf(avatar,UName))
            startActivity(intent)
        }
        viewModel.userData.observe(viewLifecycleOwner){
            avatarImg.loadImage(it.avatar)
            binding.tvUsername.text = getString(R.string.hi_string, it.name)
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting ->{
                // Not implemented here
                return true
            }
            else -> {}
        }
        return false
    }

    private fun hideAll() {
        binding.contentHome.apply {
            rvCrafts.visibility = View.GONE
            progressBar.visibility = View.GONE
            grNoSearchResult.visibility = View.GONE
            grError.visibility = View.GONE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.contentHome.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showError(msg: String) {
        Toast.makeText(
            requireContext(),
            "Gagal mengambil gambar.",
            Toast.LENGTH_SHORT
        ).show()
        hideAll()
        binding.contentHome.apply {
            grError.visibility = View.VISIBLE
            tvError.text = msg
            btnError.setOnClickListener {
                    viewModel.getCrafts()
                grError.visibility = View.GONE
            }
        }
    }
}