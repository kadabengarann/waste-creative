package com.wastecreative.wastecreative.presentation.view.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.databinding.FragmentScanResultBinding
import com.wastecreative.wastecreative.presentation.adapter.DetectedWasteListAdapter
import com.wastecreative.wastecreative.utils.getColorFromAttr

class ScanResultFragment : Fragment() {
    private var binding: FragmentScanResultBinding? = null
    private val data = ArrayList<String>()
    private val detectedWasteListAdapter: DetectedWasteListAdapter by lazy {
        DetectedWasteListAdapter(
            arrayListOf()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScanResultBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.apply {
            setSupportActionBar(binding?.toolbar)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        requireActivity().window.statusBarColor = requireContext().getColorFromAttr(com.google.android.material.R.attr.colorSecondary)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataImgUri = ScanResultFragmentArgs.fromBundle(arguments as Bundle).imgURL

        binding?.apply {
            imgScanResult.setImageURI(dataImgUri)
            seeRecommBtn.setOnClickListener{
                view.findNavController().navigate(R.id.action_navigation_scan_result_to_navigation_craft_recommendation)
            }
        }
        makeDummyData()
        showRecyclerList()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
            private fun makeDummyData() {
        if (data.isEmpty()) {
            for (i in 1..4) {
                data.add("Botol $i")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
    private fun showRecyclerList() {
        binding?.rvObjectResult?.apply {
            layoutManager = LinearLayoutManager(context)
            isVerticalScrollBarEnabled = true
            setHasFixedSize(true)
            adapter = detectedWasteListAdapter
        }
        detectedWasteListAdapter.setData(data)
    }
}