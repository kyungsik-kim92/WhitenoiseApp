package com.example.whitenoiseapp.ui.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.whitenoiseapp.adapter.TimerAdapter
import com.example.whitenoiseapp.databinding.FragmentTimerBinding
import com.example.whitenoiseapp.model.TimerModel
import com.example.whitenoiseapp.service.WhiteNoiseService
import com.example.whitenoiseapp.util.getMainActivity
import kotlinx.coroutines.launch

class TimerFragment : Fragment() {
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<TimerViewModel>()
    private val whiteNoiseService: WhiteNoiseService
        get() = getMainActivity().whiteNoiseService
    private val timerAdapter = TimerAdapter(
        isPlayingCheck = { whiteNoiseService.isPlaying() }
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
                viewModel.timerList.collect { list ->
                    timerAdapter.submitList(list)
                }
            }
        }
    }

    fun onItemClick(timerModel: TimerModel) {

    }
}