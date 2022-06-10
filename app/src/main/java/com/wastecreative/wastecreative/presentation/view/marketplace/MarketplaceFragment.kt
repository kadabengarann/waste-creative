package com.wastecreative.wastecreative.presentation.view.marketplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.data.models.model.Post
import com.wastecreative.wastecreative.databinding.FragmentMarketplaceBinding
import com.wastecreative.wastecreative.presentation.adapter.PostListAdapter

class MarketplaceFragment : Fragment() {
    private var _binding: FragmentMarketplaceBinding? = null
    private val binding get() = _binding!!

    private val postListAdapter: PostListAdapter by lazy {
        PostListAdapter(
            arrayListOf()
        )
    }
    private val data = ArrayList<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarketplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        makeDummyData()
        showRecyclerList()
        setupAction()
    }

    private fun setupAction() {
        binding.fabCreatePost.setOnClickListener {
//            view?.findNavController()?.navigate(R.id.action_navigation_craft_to_navigation_add_detail_craft)
        }
    }

    private fun makeDummyData() {
        if (data.isEmpty()) {
            for (i in 1..20) {
                val items = Post(
                    i.toString(),
                    "Saifuddin",
                    "https://picsum.photos/300/300?random=$i",
                    "Dijual cepat Kapal Mainan $i",
                    "https://picsum.photos/200/300?random=$i",
                    "yoman $i"
                )
                data.add(items)
            }
        }
    }

    private fun showRecyclerList() {
        binding.rvCrafts.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = postListAdapter
        }
        postListAdapter.setData(data)
        postListAdapter.setOnItemClickCallback(object : PostListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Post) {
                /*val intentToDetail = Intent(requireActivity(), DetailCraftActivity::class.java)
                intentToDetail.putExtra(DetailCraftActivity.EXTRA_CRAFT, data)
                startActivity(intentToDetail)*/
            }
        })
    }
}