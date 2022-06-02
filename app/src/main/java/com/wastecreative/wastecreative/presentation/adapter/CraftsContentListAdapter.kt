package com.wastecreative.wastecreative.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wastecreative.wastecreative.data.models.Craft
import com.wastecreative.wastecreative.databinding.ListItemCraftBinding
import com.wastecreative.wastecreative.databinding.ListItemCraftContentBinding
import com.wastecreative.wastecreative.utils.loadImage

class CraftsContentListAdapter(
    private val data: ArrayList<String>
): RecyclerView.Adapter<CraftsContentListAdapter.ViewHolder>() {
    fun setData(items: List<String>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemCraftContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ListItemCraftContentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            with(binding) {
                itemCraftContent.text = item
            }
        }
    }
}