package com.wastecreative.wastecreative.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wastecreative.wastecreative.databinding.ListItemInputBinding

class InputListAdapter(
    private val data: ArrayList<String>
): RecyclerView.Adapter<InputListAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: InputListAdapter.OnItemClickCallback

    fun setData(items: ArrayList<String>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }
    fun addList(newItem : String) {
        data.add(newItem)
        notifyDataSetChanged()
    }
    fun removeItem(position: Int) {
        data.removeAt(position)
        notifyDataSetChanged()
    }
    fun getData(): ArrayList<String> {
        return data
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemInputBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], position)
    }
    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ListItemInputBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, position: Int) {
            with(binding) {
                tvListItemVal.text = item
                binding.delBtn.tag = position
                binding.delBtn.setOnClickListener {
                    removeItem(position)
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
}