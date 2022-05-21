package com.wastecreative.wastecreative.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wastecreative.wastecreative.data.models.Craft
import com.wastecreative.wastecreative.databinding.ListItemCraftBinding
import com.wastecreative.wastecreative.utils.loadImage

class CraftsListAdapter(
    private val data: ArrayList<Craft>
): RecyclerView.Adapter<CraftsListAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setData(items: List<Craft>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemCraftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ListItemCraftBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userItem: Craft) {
            with(binding) {
                imgAvatar.loadImage(userItem.userPhoto, 25)
                tvListUsername.text = userItem.userName
                tvListCraftName.text = userItem.name
                imgListCraft.loadImage(userItem.photo, 40)
               /* tvItemDate.text = itemView.context.getString(
                    R.string.dateFormat,
                    userItem.createdAt.withDateFormat()
                )*/
                binding.root.setOnClickListener {
                    onItemClickCallback.onItemClicked(userItem)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Craft)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}