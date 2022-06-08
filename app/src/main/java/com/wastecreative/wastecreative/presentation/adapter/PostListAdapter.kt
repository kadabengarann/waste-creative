package com.wastecreative.wastecreative.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wastecreative.wastecreative.data.models.model.Post
import com.wastecreative.wastecreative.databinding.ListItemPostBinding
import com.wastecreative.wastecreative.utils.loadImage

class PostListAdapter(
    private val data: ArrayList<Post>
): RecyclerView.Adapter<PostListAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setData(items: List<Post>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ListItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(postItem: Post) {
            with(binding) {
                imgAvatar.loadImage(postItem.userPhoto, 25)
                tvListUsername.text = postItem.userName
                tvListPostTitle.text = postItem.title
                imgListCraft.loadImage(postItem.photo, 40)
                binding.root.setOnClickListener {
                    onItemClickCallback.onItemClicked(postItem)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Post)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}