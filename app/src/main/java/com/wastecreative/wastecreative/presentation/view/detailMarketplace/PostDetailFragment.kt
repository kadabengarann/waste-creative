package com.wastecreative.wastecreative.presentation.view.detailMarketplace

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.wastecreative.wastecreative.data.models.MarketplaceDetail
import com.wastecreative.wastecreative.data.network.Result
import com.wastecreative.wastecreative.databinding.FragmentPostDetailBinding

class PostDetailFragment : Fragment() {

    private var binding: FragmentPostDetailBinding? = null

    private lateinit var viewModel : DetailMarketplaceViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[DetailMarketplaceViewModel::class.java]

        observe()
    }

    private fun observe() {
        viewModel.marketplaceDetail.observe(requireActivity()) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                        hideALl()
                    }
                    is Result.Success -> {
                        showLoading(false)
                        setContent(result.data)
                    }
                    is Result.Error -> {
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun setContent(data: MarketplaceDetail) {
        binding?.apply {
            grContent.visibility = View.VISIBLE
            postDescriptionTv.visibility = View.VISIBLE
            postDescriptionTv.text = data.description
            postPriceTv.text = data.price.toString()
            postAddressTv.text = data.description
        }
    }

    private fun hideALl() {
        binding?.apply {
            grContent.visibility = View.GONE
        }
    }
    private fun showLoading(b: Boolean) {
        binding?.progressBar?.visibility = if (b) View.VISIBLE else View.GONE
    }
}