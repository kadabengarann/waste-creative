package com.wastecreative.wastecreative.presentation.view.craft

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.data.models.CraftDetail
import com.wastecreative.wastecreative.data.network.Result
import com.wastecreative.wastecreative.databinding.FragmentDetailCraftTabBinding
import com.wastecreative.wastecreative.presentation.adapter.CraftsContentListAdapter

class DetailCraftTabFragment : Fragment() {
    private var binding: FragmentDetailCraftTabBinding? = null

    private var index = 0
    private lateinit var viewModel : DetailCraftViewModel
    private val contentListAdapter: CraftsContentListAdapter by lazy {
        CraftsContentListAdapter(
            ArrayList()
        )
    }
    private val contentListAdapter2: CraftsContentListAdapter by lazy {
        CraftsContentListAdapter(
            ArrayList()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailCraftTabBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[DetailCraftViewModel::class.java]
        index = arguments?.getInt(ARG_SECTION_NUMBER, 0)!!
        if (arguments != null) {
            when (index) {
                1 -> {
                    observeMaterialsContent()
                }
                2 -> {
                    observeStepsContent()
                }
            }
        }
    }

    private fun observeMaterialsContent() {
        viewModel.craftDetail.observe(requireActivity()) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        hideALl()
                    }
                    is Result.Success -> {
                        setMaterialContent(result.data)
                    }
                    is Result.Error -> {
                    }
                }
            }
        }
    }

    private fun setMaterialContent(data: CraftDetail) {
        binding?.apply {
            tvHeadingTop.visibility = View.VISIBLE
            tvHeadingBottom.visibility = View.VISIBLE
            tvHeadingTop.text = getString(R.string.tools_string)
            rvCraftTab.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = contentListAdapter
            }

            tvHeadingBottom.text = getString(R.string.materials_strings)
            rvCraftTab2.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = contentListAdapter2
            }
        }
        contentListAdapter.setData(data.tools)
        contentListAdapter2.setData(data.materials)
    }

    private fun observeStepsContent() {
        viewModel.craftDetail.observe(requireActivity()) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        hideALl()
                    }
                    is Result.Success -> {
                        setStepsContent(result.data)
                    }
                    is Result.Error -> {
                    }
                }
            }
        }
    }

    private fun hideALl() {
        binding?.apply {
            tvHeadingTop.visibility = View.GONE
            tvHeadingBottom.visibility = View.GONE
        }
    }

    private fun setStepsContent(data: CraftDetail) {
        binding?.apply {
            tvHeadingTop.visibility = View.VISIBLE
            tvHeadingBottom.visibility = View.VISIBLE
            tvHeadingTop.text = resources.getString(R.string.tab_steps_text)
            rvCraftTab.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = contentListAdapter
            }
            tvHeadingBottom.visibility = View.VISIBLE
            tvHeadingBottom.text = "Video tutorial"
            tvLinkVideo.visibility = View.VISIBLE
            if (data.video != "" && data.video != null){
                tvLinkVideo.text = getString(R.string.link_string)
                tvLinkVideo.setTextColor(Color.BLUE)
                tvLinkVideo.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                tvLinkVideo.setOnClickListener {
                    val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data.video))
                    startActivity(urlIntent)
                }

            }else{
                tvLinkVideo.text = "---"
            }
            rvCraftTab2.visibility = View.GONE
        }
        contentListAdapter.setData(data.steps)
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
    }
}