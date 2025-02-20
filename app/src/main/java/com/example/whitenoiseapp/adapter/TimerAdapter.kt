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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class TimerAdapter(
    private val isPlayingCheck: () -> Boolean,
    private val selectUnit: (TimerModel) -> Unit
) : ListAdapter<TimerModel, TimerViewHolder>(diffUtil) {
    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val binding =
            ItemTimerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimerViewHolder(
            binding,
            onItemClick = { position ->
                handleSelection(position)
            },
            isPlayingCheck = isPlayingCheck
        )
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.bind(getItem(position))
        if (getItem(position).isSelected.value) {
            selectedPosition = position
        }
    }

    override fun submitList(list: List<TimerModel>?) {
        // 리스트 제출 시 선택된 아이템 확인
        list?.forEachIndexed { index, item ->
            if (item.isSelected.value) {
                selectedPosition = index
            }
        }
        super.submitList(list)
    }

    private fun handleSelection(newPosition: Int) {
        if (selectedPosition == newPosition) return

        if (selectedPosition != RecyclerView.NO_POSITION) {
            getItem(selectedPosition).setIsSelected(false)
        }

        getItem(newPosition).setIsSelected(true)
        selectedPosition = newPosition
        selectUnit(getItem(newPosition))
    }


    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<TimerModel>() {
            override fun areItemsTheSame(oldItem: TimerModel, newItem: TimerModel) =
                oldItem === newItem

            override fun areContentsTheSame(oldItem: TimerModel, newItem: TimerModel) =
                oldItem === newItem

        }
    }
}

class TimerViewHolder(
    private val binding: ItemTimerBinding,
    private val onItemClick: (Int) -> Unit,
    private val isPlayingCheck: () -> Boolean
) : RecyclerView.ViewHolder(binding.root) {
    private var job: Job? = null

    init {
        binding.root.setOnClickListener {
            val position = adapterPosition
            if (!isPlayingCheck()) {
                Toast.makeText(it.context, "음악을 선택해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (position != RecyclerView.NO_POSITION) {
                onItemClick(position)
            }

        }
    }

    fun bind(item: TimerModel) {
        job?.cancel()

        job = CoroutineScope(Dispatchers.Main).launch {
            item.isSelected.collect { isSelected ->
                binding.root.isSelected = isSelected
                // 선택 상태에 따른 UI 업데이트
                binding.btnSelector.setBackgroundResource(
                    if (isSelected) R.drawable.ic_clamp_selected
                    else R.drawable.ic_clamp_unselected
                )
            }
        }

        binding.timerModel = item
        binding.executePendingBindings()
    }
}