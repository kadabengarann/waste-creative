package com.wastecreative.wastecreative.presentation.view.profile

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater

import android.view.ViewGroup
import android.widget.TextView

import com.wastecreative.wastecreative.databinding.FragmentImarketBinding

import com.wastecreative.wastecreative.presentation.view.profile.placeholder.PlaceholderContent.PlaceholderItem



class MarketViewAdapter(
    private val values: List<PlaceholderItem>
) : RecyclerView.Adapter<MarketViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentImarketBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentImarketBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}