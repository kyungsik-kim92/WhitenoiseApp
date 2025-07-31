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
import com.example.whitenoiseapp.MainUiEvent
import com.example.whitenoiseapp.MainUiState
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
        onTimerSelected = ::setTimerSelected
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
        setupRecyclerView()
        observeUiState()
        observeEvents()
    }


    private fun setupRecyclerView() {
        binding.rvTimerList.adapter = timerAdapter
        binding.rvTimerList.itemAnimator = null
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.timerList.collect { timerList ->
                    timerAdapter.submitList(timerList)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.uiState.collect { mainState ->
                    when (mainState) {
                        is MainUiState.Success -> {
                            if (mainState.isServiceReady) {
                                mainViewModel.observeTimerState(whiteNoiseService.timerState)
                            }
                        }

                        is MainUiState.Error -> {}

                        else -> {}
                    }
                }
            }
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.events.collect { event ->
                    when (event) {
                        is MainUiEvent.ShowError -> {
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun setTimerSelected(index: Int) {
        if (!whiteNoiseService.isPlaying()) {
            Toast.makeText(context, "음악을 선택해 주세요", Toast.LENGTH_SHORT).show()
            return
        }
        mainViewModel.selectTimer(index)
        val selectedTimer = mainViewModel.getSelectedTimer()
        selectedTimer?.let { timer ->
            val timeToSet = if (timer.ms > 0L) timer.ms else 0L
            whiteNoiseService.setupTimer(timeToSet)
            val message = if (timer.ms > 0L) {
                "The timer has been set.."
            } else {
                "The timer has been cleared."
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}