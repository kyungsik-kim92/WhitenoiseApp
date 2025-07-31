package com.example.whitenoiseapp.ui.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.whitenoiseapp.MainUiState
import com.example.whitenoiseapp.MainViewModel
import com.example.whitenoiseapp.adapter.PlayAdapter
import com.example.whitenoiseapp.databinding.FragmentPlayBinding
import com.example.whitenoiseapp.service.WhiteNoiseService
import com.example.whitenoiseapp.util.getMainActivity
import kotlinx.coroutines.launch

class PlayFragment : Fragment() {
    private var _binding: FragmentPlayBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()
    private val viewModel by viewModels<PlayViewModel>()
    private lateinit var whiteNoiseService: WhiteNoiseService
    private val playAdapter = PlayAdapter(
        onItemClick = { index ->
            viewModel.togglePlaySelection(index)
        }
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeUiState()
        observeEvents()
        viewModel.observeTimerFinished(mainViewModel)
    }


    private fun setupRecyclerView() {
        binding.rvPlayList.adapter = playAdapter
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playList.collect { playList ->
                    playAdapter.submitList(playList)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is PlayUiState.Loading -> {}
                        is PlayUiState.Success -> {
                            updateTimerUI(uiState)
                            if (uiState.isServiceReady) {
                                initializeService()
                            }
                        }

                        is PlayUiState.Error -> {}
                    }
                }
            }
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.uiState.collect { mainState ->
                    when (mainState) {
                        is MainUiState.Success -> {
                            viewModel.updateServiceReady(mainState.isServiceReady)
                        }

                        is MainUiState.Error -> {}
                        else -> {}
                    }
                }
            }
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.timerList.collect { timerList ->
                    val selectedTimer = timerList.find { it.isSelected }
                    viewModel.updateTimerInfo(selectedTimer, mainViewModel.realTime.value)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.realTime.collect { realTime ->
                    val selectedTimer = mainViewModel.getSelectedTimer()
                    viewModel.updateTimerInfo(selectedTimer, realTime)
                }
            }
        }
    }


    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is PlayUiEvent.PlaySelected -> {
                            setPlaySelection(event.index, event.isSelected)
                        }

                        is PlayUiEvent.ShowToast -> {
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun updateTimerUI(uiState: PlayUiState.Success) {
        if (uiState.isTimerVisible && uiState.currentTimer != null) {
            binding.tvScheduleTime.text = mainViewModel.formatTime(uiState.scheduledTime, "SET")
            binding.tvRealTime.text = mainViewModel.formatTime(uiState.realTime, "TIMER")
            binding.layoutTime.visibility = View.VISIBLE
            binding.timerGroup.visibility = View.VISIBLE
        } else {
            binding.layoutTime.visibility = View.GONE
            binding.timerGroup.visibility = View.GONE
        }
    }


    private fun setPlaySelection(index: Int, isSelected: Boolean) {
        if (::whiteNoiseService.isInitialized) {
            if (isSelected) {
                whiteNoiseService.startMediaPlayer(index)
            } else {
                whiteNoiseService.stopMediaPlayer(index)
            }
        }
    }

    private fun initializeService() {
        if (::whiteNoiseService.isInitialized.not()) {
            whiteNoiseService = getMainActivity().whiteNoiseService
            mainViewModel.observeTimerState(whiteNoiseService.timerState)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}