package com.example.whitenoiseapp.ui.play

import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.whitenoiseapp.R
import com.example.whitenoiseapp.model.PlayModel
import com.example.whitenoiseapp.ui.main.MainActivity
import com.example.whitenoiseapp.ui.main.MainUiState
import com.example.whitenoiseapp.ui.main.MainViewModel

@Composable
fun PlayScreen(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val playViewModel = hiltViewModel<PlayViewModel>()

    val playList by playViewModel.playList.collectAsState()
    val uiState by playViewModel.uiState.collectAsState()
    val mainUiState by mainViewModel.uiState.collectAsState()
    val timerList by mainViewModel.timerList.collectAsState()
    val realTime by mainViewModel.realTime.collectAsState()

    val context = LocalContext.current
    val mainActivity = context as? MainActivity

    LaunchedEffect(mainUiState) {
        val state = mainUiState
        when (state) {
            is MainUiState.Success -> {
                playViewModel.updateServiceReady(state.isServiceReady)
            }

            else -> {}
        }
    }

    LaunchedEffect(timerList, realTime) {
        val selectedTimer = timerList.find { it.isSelected }
        playViewModel.updateTimerInfo(selectedTimer, realTime)
    }

    LaunchedEffect(mainUiState) {
        when (val state = mainUiState) {
            is MainUiState.Success -> {
                if (state.isServiceReady) {
                    mainActivity?.getServiceInstance()?.let { service ->
                        mainViewModel.observeTimerState(service.timerState)
                    }
                }
            }

            else -> {}
        }
    }

    LaunchedEffect(Unit) {
        playViewModel.observeTimerFinished(mainViewModel)
    }

    LaunchedEffect(Unit) {
        playViewModel.events.collect { event ->
            when (event) {
                is PlayUiEvent.PlaySelected -> {
                    mainActivity?.getServiceInstance()?.let { service ->
                        if (event.isSelected) {
                            service.startMediaPlayer(event.index)
                        } else {
                            service.stopMediaPlayer(event.index)
                        }
                    }
                }

                is PlayUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    when (val state = uiState) {
        is PlayUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is PlayUiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorResource(R.color.green_200),
                                Color(0xFF1A1A1A)  // 더 어두운 그라데이션
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Text(
                    text = "White Noise",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(bottom = 20.dp)
                )


                val selectedTimer = timerList.find { it.isSelected }
                if (selectedTimer != null && selectedTimer.isSelected) {
                    TimerInfoCard(
                        scheduledTime = mainViewModel.formatTime(selectedTimer.ms, "SET"),
                        realTime = mainViewModel.formatTime(realTime, "TIMER"),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }


                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    itemsIndexed(playList) { index, play ->
                        PlayCard(
                            play = play,
                            onItemClick = {
                                playViewModel.togglePlaySelection(index)
                            }
                        )
                    }
                }
            }
        }

        is PlayUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun TimerInfoCard(
    scheduledTime: String,
    realTime: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF37474F)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "SET TIME",
                    color = Color(0xFF9E9E9E),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = scheduledTime,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "REMAINING",
                    color = Color(0xFF9E9E9E),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = realTime,
                    color = Color(0xFF4CAF50),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PlayCard(
    play: PlayModel,
    onItemClick: () -> Unit
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (play.isSelected) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    val animatedElevation by animateDpAsState(
        targetValue = if (play.isSelected) 12.dp else 6.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "elevation"
    )

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .size(70.dp)
            .scale(animatedScale)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onItemClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = animatedElevation),
        colors = CardDefaults.cardColors(
            containerColor = if (play.isSelected) {
                Color(0xFF26A69A)
            } else {
                Color(0xFF37474F)
            }
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (play.isSelected) {
                                Color.White.copy(alpha = 0.2f)
                            } else {
                                Color.Transparent
                            },
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(play.iconResId),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        colorFilter = ColorFilter.tint(
                            if (play.isSelected) Color.White else Color(0xFFB0B0B0)
                        )
                    )
                }

                if (play.isSelected) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Playing",
                        modifier = Modifier.size(50.dp),
                        tint = Color.White
                    )
                }
            }

            if (play.isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0x1A4CAF50),
                                    Color.Transparent
                                ),
                                radius = 100f
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                )
            }
        }
    }
}