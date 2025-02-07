package com.example.whitenoiseapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.whitenoiseapp.databinding.FragmentPlayBinding

class PlayFragment : Fragment() {
    private var _binding: FragmentPlayBinding? = null
    private val binding get() = _binding!!
    private val playAdapter by lazy {
        PlayAdapter { index, isSelected ->
            onItemClick(index, isSelected)
        }
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
        val data = listOf(
            PlayModel(
                musicResId = R.raw.rain_window,
                iconResId = R.drawable.round_window_24
            ),
            PlayModel(
                musicResId = R.raw.river,
                iconResId = R.drawable.river
            ),
            PlayModel(
                musicResId = R.raw.car,
                iconResId = R.drawable.round_directions_car_24
            ),
        )
        binding.rvPlayList.adapter = playAdapter
        playAdapter.submitList(data)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemClick(index: Int, isSelected: Boolean) {

    }
}