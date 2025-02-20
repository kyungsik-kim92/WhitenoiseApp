package com.example.whitenoiseapp.ui.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.whitenoiseapp.MainViewModel
import com.example.whitenoiseapp.adapter.TimerAdapter
import com.example.whitenoiseapp.databinding.FragmentTimerBinding
import com.example.whitenoiseapp.model.TimerModel
import com.example.whitenoiseapp.service.WhiteNoiseService
import com.example.whitenoiseapp.util.getMainActivity
import kotlinx.coroutines.launch

class TimerFragment : Fragment() {
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()
    private val whiteNoiseService: WhiteNoiseService
        get() = getMainActivity().whiteNoiseService
    private val timerAdapter = TimerAdapter(
        isPlayingCheck = { whiteNoiseService.isPlaying() },
        selectUnit = { timerModel -> selectUnit(timerModel) }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.observeTimerState(whiteNoiseService.timerState)
        binding.rvTimerList.adapter = timerAdapter
        binding.rvTimerList.itemAnimator = null
        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.scheduledTime.collect { list ->
                    timerAdapter.submitList(list)
                }
            }
        }
    }

    private fun selectUnit(timerModel: TimerModel) {
        val isPlaying = timerModel.ms > 0L
        if (isPlaying) {
            Toast.makeText(this.context, "The timer has been set..", Toast.LENGTH_SHORT).show()
            whiteNoiseService.setupTimer(timerModel.ms)

        } else {
            Toast.makeText(this.context, "The timer has been cleared.", Toast.LENGTH_SHORT)
                .show()
            whiteNoiseService.setupTimer(0L)
        }
    }
}