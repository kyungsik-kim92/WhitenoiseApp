package com.example.whitenoiseapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.whitenoiseapp.R
import com.example.whitenoiseapp.databinding.ItemTimerBinding
import com.example.whitenoiseapp.model.TimerModel


class TimerAdapter(
    private val isPlayingCheck: () -> Boolean,
    private val onTimerSelected: (Int) -> Unit
) : ListAdapter<TimerModel, TimerViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val binding =
            ItemTimerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimerViewHolder(
            binding,
            onItemClick = { position ->
                if (!isPlayingCheck()) {
                    Toast.makeText(
                        binding.root.context,
                        "음악을 선택해 주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@TimerViewHolder
                }
                onTimerSelected(position)
            }
        )
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
    private val onItemClick: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClick(position)
            }
        }
    }

    fun bind(item: TimerModel) {
        binding.timerModel = item

        binding.root.isSelected = item.isSelected
        binding.btnSelector.setBackgroundResource(
            if (item.isSelected) R.drawable.ic_clamp_selected
            else R.drawable.ic_clamp_unselected
        )

        binding.executePendingBindings()
    }
}