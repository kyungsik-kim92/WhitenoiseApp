package com.example.whitenoiseapp.ui.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.whitenoiseapp.MainViewModel
import com.example.whitenoiseapp.R
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
        onItemClick = { index, isSelected ->
            onItemClick(index, isSelected)
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
        viewModel.observeSharedEvents(mainViewModel)
        observeService()
        binding.rvPlayList.adapter = playAdapter
        setupRecyclerView()
        observeTimerState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playList.collect { list ->
                    playAdapter.submitList(list)
                }
            }
        }
    }

    private fun observeService() {
        lifecycleScope.launch {
            mainViewModel.isServiceReady.collect { isReady ->
                if (isReady) {
                    whiteNoiseService = getMainActivity().whiteNoiseService
                    mainViewModel.observeTimerState(whiteNoiseService.timerState)
                }
            }
        }
    }

    private fun observeTimerState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.scheduledTime.collect { scheduledTime ->
                    scheduledTime.filter { timerModel -> timerModel.isSelected.value }
                        .forEach { timerModel ->
                            with(binding.tvScheduleTime) {
                                val hour = timerModel.ms / 3600000
                                val minute = timerModel.ms % 3600000 / 60000
                                val second = timerModel.ms % 3600000 % 60000 / 1000
                                text =
                                    getString(R.string.schedule_time_format, hour, minute, second)


                            }
                            binding.layoutTime.visibility = View.VISIBLE
                            binding.timerGroup.visibility = View.VISIBLE
                        }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.realTime.collect { ms ->
                    with(binding.tvRealTime) {
                        val hour = ms / 3600000
                        val minute = ms % 3600000 / 60000
                        val second = ms % 3600000 % 60000 / 1000
                        text = getString(R.string.real_time_format, hour, minute, second)
                    }
                }
            }
        }
    }


    private fun onItemClick(index: Int, isSelected: Boolean) {
        if (isSelected) {
            getMainActivity().whiteNoiseService.startMediaPlayer(index)
        } else {
            getMainActivity().whiteNoiseService.stopMediaPlayer(index)
        }
    }
}