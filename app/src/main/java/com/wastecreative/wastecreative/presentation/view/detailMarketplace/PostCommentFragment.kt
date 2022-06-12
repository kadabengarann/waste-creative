package com.wastecreative.wastecreative.presentation.view.detailMarketplace

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wastecreative.wastecreative.R
import com.wastecreative.wastecreative.data.models.MarketplaceComment
import com.wastecreative.wastecreative.data.network.Result
import com.wastecreative.wastecreative.databinding.FragmentPostCommentBinding
import com.wastecreative.wastecreative.databinding.FragmentPostDetailBinding
import com.wastecreative.wastecreative.presentation.adapter.CommentListAdapter
import com.wastecreative.wastecreative.presentation.adapter.CraftsContentListAdapter
import com.wastecreative.wastecreative.presentation.adapter.LoadingStateAdapter
import com.wastecreative.wastecreative.utils.hideKeyboard


class PostCommentFragment : Fragment() {
    private var binding: FragmentPostCommentBinding? = null

    private lateinit var viewModel : DetailMarketplaceViewModel

    private var comment: String =""
    private val commentListAdapter: CommentListAdapter by lazy {
        CommentListAdapter(
            ArrayList()
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPostCommentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[DetailMarketplaceViewModel::class.java]
        binding?.rvComment?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = commentListAdapter
        }
        setupAction()
        setupViewModel()
        observe()
    }

    private fun setupAction() {
        binding?.apply {
            commentInput.doOnTextChanged { text, _, _, _ ->
                comment = text.toString()
                if (validateComment()){
                    //enable btn
                    commentSendBtn.visibility = View.VISIBLE
                    commentSendBtn.isEnabled = true
                    commentSendBtn.isClickable = true
                }else{
                    //disable btn
                    commentSendBtn.visibility = View.GONE
                    commentSendBtn.isEnabled = false
                    commentSendBtn.isClickable = false
                }
            }
            commentSendBtn.setOnClickListener {
                //send comment
                viewModel.sendComment(comment)
            }
        }

    }

    private fun setupViewModel() {
        viewModel.fetchComments()
    }

    private fun observe() {
        viewModel.comments.observe(requireActivity()) { result ->
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
                        showError()
                    }
                }
            }
        }
        viewModel.uploadResult.observe(requireActivity()) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        clearInput()
                        viewModel.fetchComments()
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showErrorMsg(result.error)
                    }
                }
            }
        }
    }

    private fun setContent(data: List<MarketplaceComment>) {
        if (data.isNotEmpty()){
        binding?.rvComment?.apply {
            visibility = View.VISIBLE
            smoothScrollToPosition(0)
        }
        commentListAdapter.setData(data)
        }else{
            binding?.tvEmpty?.visibility = View.VISIBLE
        }

    }

    private fun hideALl() {
        binding?.tvEmpty?.visibility = View.GONE
        binding?.rvComment?.visibility = View.GONE
    }

    private fun showError() {
        binding?.btnError?.setOnClickListener {
            binding?.grError?.visibility = View.GONE
            viewModel.fetchComments()
        }
    }
    private fun showErrorMsg(error: String) {
        AlertDialog.Builder(requireContext(), R.style.AlertDialog).apply {
            setTitle("Failed")
            setMessage(error)
            setNegativeButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun showLoading(b: Boolean) {
        binding?.progressBar?.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun clearInput(){
        binding?.commentInput?.text = null
        binding?.commentInput?.clearFocus()
        hideKeyboard()

    }
    private fun validateComment() : Boolean{
        val comment = binding?.commentInput?.text.toString().isEmpty()

        return !comment

    }
}