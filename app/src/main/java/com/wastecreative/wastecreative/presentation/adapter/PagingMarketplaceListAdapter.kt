package com.wastecreative.wastecreative.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wastecreative.wastecreative.data.database.CraftEntity
import com.wastecreative.wastecreative.data.database.MarketplaceEntity
import com.wastecreative.wastecreative.databinding.ListItemPostBinding
import com.wastecreative.wastecreative.utils.formatK
import com.wastecreative.wastecreative.utils.loadImage

class PagingMarketplaceListAdapter:
    PagingDataAdapter<MarketplaceEntity, PagingMarketplaceListAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class ViewHolder(private val binding: ListItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(marketplaceItem: MarketplaceEntity) {
            with(binding) {
                imgAvatar.loadImage(marketplaceItem.userPhoto, 25)
                tvListUsername.text = marketplaceItem.userName
                tvListPostTitle.text = marketplaceItem.judul
                imgListCraft.loadImage(marketplaceItem.foto, 40)
                tvListLikes.text = "${marketplaceItem.like.formatK()} likes"
                binding.root.setOnClickListener {
                    onItemClickCallback.onItemClicked(marketplaceItem.id)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: String)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MarketplaceEntity>() {
            override fun areItemsTheSame(oldItem: MarketplaceEntity, newItem: MarketplaceEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MarketplaceEntity, newItem: MarketplaceEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}