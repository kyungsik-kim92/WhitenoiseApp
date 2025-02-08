package com.example.whitenoiseapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whitenoiseapp.databinding.ItemPlayBinding

class PlayAdapter(
    private val onItemClick: (index: Int, isSelected: Boolean) -> Unit
) : ListAdapter<PlayModel, PlayViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayViewHolder {
        val binding =
            ItemPlayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: PlayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<PlayModel>() {
            override fun areItemsTheSame(oldItem: PlayModel, newItem: PlayModel) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: PlayModel, newItem: PlayModel) =
                oldItem == newItem
        }
    }

}

class PlayViewHolder(
    private val binding: ItemPlayBinding,
    private val onItemClick: (index: Int, isSelected: Boolean) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: PlayModel) {
        binding.playModel = item
        Glide.with(itemView).load(item.iconResId).into(binding.ivIcon)
        itemView.setOnClickListener {
            item.setIsSelected(!item.isSelected.value)
            onItemClick(layoutPosition, item.isSelected.value)
            binding.invalidateAll()
        }
    }
}