package com.example.whitenoiseapp.ui.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.whitenoiseapp.adapter.PlayAdapter
import com.example.whitenoiseapp.databinding.FragmentPlayBinding
import com.example.whitenoiseapp.util.getMainActivity
import kotlinx.coroutines.launch

class PlayFragment : Fragment() {
    private var _binding: FragmentPlayBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PlayViewModel>()
    private val playAdapter = PlayAdapter { index, isSelected ->
        onItemClick(index, isSelected)
    }


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

        binding.rvPlayList.adapter = playAdapter
        setupRecyclerView()


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

    private fun onItemClick(index: Int, isSelected: Boolean) {
        if (isSelected) {
            getMainActivity().whiteNoiseService.startMediaPlayer(index)
        } else {
            getMainActivity().whiteNoiseService.stopMediaPlayer(index)
        }
    }
}