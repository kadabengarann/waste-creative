package com.wastecreative.wastecreative.presentation.view.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.data.models.Craft
import com.wastecreative.wastecreative.databinding.FragmentHomeBinding
import com.wastecreative.wastecreative.presentation.adapter.CraftsListAdapter
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
    private val data = ArrayList<Craft>()

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
        makeDummyData()
        showRecyclerList()
        setupAction()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun makeDummyData() {
        if (data.isEmpty()) {
            for (i in 1..4) {
                val items = Craft(
                    i.toString(),
                    "Saifuddin",
                    "https://picsum.photos/300/300?random=$i",
                    69,
                    "yoman $i",
                    "https://picsum.photos/200/300?random=$i"
                )
                data.add(items)
            }
        }
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
        binding.contentHome.rvCrafts.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = craftListAdapter
        }
        craftListAdapter.setData(data)
        craftListAdapter.setOnItemClickCallback(object : CraftsListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                val intentToDetail = Intent(requireActivity(), DetailCraftActivity::class.java)
                intentToDetail.putExtra(DetailCraftActivity.EXTRA_CRAFT, data)
                startActivity(intentToDetail)
            }
        })
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