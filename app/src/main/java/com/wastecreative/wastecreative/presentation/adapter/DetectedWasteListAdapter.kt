package com.wastecreative.wastecreative.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wastecreative.wastecreative.databinding.ListItemDetectedWasteBinding

class DetectedWasteListAdapter(
    private val data: ArrayList<String>
): RecyclerView.Adapter<DetectedWasteListAdapter.ViewHolder>() {
    fun setData(items: List<String>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemDetectedWasteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ListItemDetectedWasteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            with(binding) {
                tvListWasteName.text= item
            }
        }
    }

}