package com.wastecreative.wastecreative.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wastecreative.wastecreative.data.models.MarketplaceComment
import com.wastecreative.wastecreative.databinding.ListItemCommentBinding
import com.wastecreative.wastecreative.utils.loadImage

class CommentListAdapter(
    private val data: ArrayList<MarketplaceComment>
): RecyclerView.Adapter<CommentListAdapter.ViewHolder>() {

    fun setData(items: List<MarketplaceComment>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ListItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: MarketplaceComment) {
            with(binding) {
                imgAvatar.loadImage(comment.userPhoto, 25)
                tvListUsername.text = comment.userName
                tvListComment.text = comment.comment
            }
        }
    }
}