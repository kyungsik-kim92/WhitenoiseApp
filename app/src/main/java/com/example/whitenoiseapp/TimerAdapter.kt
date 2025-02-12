package com.example.whitenoiseapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.whitenoiseapp.databinding.ItemTimerBinding

class TimerAdapter(
    private val onItemClick: (index: Int) -> Unit
) : ListAdapter<TimerModel, TimerViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val binding =
            ItemTimerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimerViewHolder(binding, onItemClick, ::getCurrentList)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<TimerModel>() {
            override fun areItemsTheSame(oldItem: TimerModel, newItem: TimerModel) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: TimerModel, newItem: TimerModel) =
                oldItem == newItem

        }
    }
}

class TimerViewHolder(
    private val binding: ItemTimerBinding,
    private val onItemClick: (index: Int) -> Unit,
    private val getCurrentList: () -> List<TimerModel>
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: TimerModel) {
        binding.timerModel = item
        itemView.setOnClickListener {
            if (itemView.isSelected) {
                return@setOnClickListener
            }
            getCurrentList().map { !it.isSelected.value }
            item.setIsSelected(true)
            onItemClick(layoutPosition)
        }
        binding.invalidateAll()
    }
}