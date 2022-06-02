package com.wastecreative.wastecreative.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wastecreative.wastecreative.data.database.CraftEntity
import com.wastecreative.wastecreative.databinding.ListItemCraftBinding
import com.wastecreative.wastecreative.utils.formatK
import com.wastecreative.wastecreative.utils.loadImage

class PagingCraftsListAdapter:
    PagingDataAdapter<CraftEntity, PagingCraftsListAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemCraftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class ViewHolder(private val binding: ListItemCraftBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(craftItem: CraftEntity) {
            with(binding) {
                imgAvatar.loadImage(craftItem.userPhoto, 25)
                tvListUsername.text = craftItem.userName
                tvListCraftName.text = craftItem.name
                imgListCraft.loadImage(craftItem.photo, 40)
                tvListLikes.text = "${craftItem.like.formatK()} likes"
                binding.root.setOnClickListener {
                    onItemClickCallback.onItemClicked(craftItem)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: CraftEntity)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CraftEntity>() {
            override fun areItemsTheSame(oldItem: CraftEntity, newItem: CraftEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CraftEntity, newItem: CraftEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}