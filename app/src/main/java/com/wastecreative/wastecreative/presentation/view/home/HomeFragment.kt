package com.wastecreative.wastecreative.presentation.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.data.models.model.Craft
import com.wastecreative.wastecreative.databinding.FragmentHomeBinding
import com.wastecreative.wastecreative.presentation.adapter.CraftsListAdapter
import com.wastecreative.wastecreative.presentation.view.craft.DetailCraftActivity
import com.wastecreative.wastecreative.presentation.view.scan.ScanActivity


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
                    "Kapal Mainan $i",
                    "https://picsum.photos/200/300?random=$i",
                    "yoman $i"
                )
                data.add(items)
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            ctaHome.setOnClickListener{
                startActivity(Intent(requireActivity(), ScanActivity::class.java))
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
            override fun onItemClicked(data: Craft) {
                val intentToDetail = Intent(requireActivity(), DetailCraftActivity::class.java)
                intentToDetail.putExtra(DetailCraftActivity.EXTRA_CRAFT, data)
                startActivity(intentToDetail)
            }
        })
    }

}