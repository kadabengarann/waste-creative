package com.wastecreative.wastecreative.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wastecreative.wastecreative.data.models.Craft
import com.wastecreative.wastecreative.databinding.ListItemCraftBinding
import com.wastecreative.wastecreative.utils.loadImage
import java.util.ArrayList


class KerajinanViewAdapter(
    private val data :ArrayList<Craft>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setData(items: List<Craft>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ListItemCraftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return when (viewType) {
            TYPE_WITH_USER -> WithUserViewHolder(binding)
            else -> WithoutUserViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        if (getItemViewType(position) == TYPE_WITH_USER) (holder as WithUserViewHolder).bind(data[position])
        else (holder as WithoutUserViewHolder).bind(data[position])


    override fun getItemCount(): Int = data.size


    override fun getItemViewType(position: Int): Int {
        return when (data[position].userName) {
            null -> TYPE_NO_USER
            else -> TYPE_WITH_USER
        }
    }
    companion object {
        private const val TYPE_NO_USER = 0
        private const val TYPE_WITH_USER = 1
    }

    inner class WithUserViewHolder(private val binding: ListItemCraftBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userItem: Craft) {
            with(binding) {
                imgAvatar.loadImage(userItem.userPhoto, 25)
                tvListUsername.text = userItem.userName
                tvListCraftName.text = userItem.name
                imgListCraft.loadImage(userItem.photo, 40)
                binding.root.setOnClickListener {

                }
            }
        }
    }
    inner class WithoutUserViewHolder(private val binding: ListItemCraftBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userItem: Craft) {
            with(binding) {
                grUserData.visibility = View.GONE
                tvListCraftName.text = userItem.name
                imgListCraft.loadImage(userItem.photo, 40)

            }
        }
    }


}