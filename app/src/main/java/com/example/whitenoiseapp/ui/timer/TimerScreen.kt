package com.example.whitenoiseapp.ui.timer

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.whitenoiseapp.R
import com.example.whitenoiseapp.core.ui.composables.AnimatedCard
import com.example.whitenoiseapp.core.ui.composables.GradientBackground
import com.example.whitenoiseapp.domain.model.TimerModel
import com.example.whitenoiseapp.ui.main.MainUiEvent
import com.example.whitenoiseapp.ui.main.MainUiState
import com.example.whitenoiseapp.ui.main.MainViewModel


@Composable
fun TimerScreen(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val timerList by mainViewModel.timerList.collectAsState()
    val mainUiState by mainViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(mainUiState) {
        val state = mainUiState
        when (state) {
            is MainUiState.Success -> {
                if (state.isServiceReady) {
                    mainViewModel.observeTimerState()
                }
            }

            else -> {}
        }
    }

    LaunchedEffect(Unit) {
        mainViewModel.events.collect { event ->
            when (event) {
                is MainUiEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Sleep Timer",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(timerList.size) { index ->
                    val timer = timerList[index]
                    TimerCard(
                        timer = timer,
                        onTimerClick = {
                            mainViewModel.selectTimer(index)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TimerCard(
    timer: TimerModel,
    onTimerClick: () -> Unit
) {
    AnimatedCard(
        modifier = Modifier
            .aspectRatio(1.2f),
        isSelected = timer.isSelected,
        onClick = onTimerClick,
        shape = RoundedCornerShape(20.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (timer.ms == 0L) ImageVector.vectorResource(R.drawable.ic_block) else ImageVector.vectorResource(
                        R.drawable.ic_alarm
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = if (timer.isSelected) Color.White else Color(0xFFB0B0B0)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = timer.timerStr,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (timer.isSelected) Color.White else Color(0xFFE0E0E0),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                if (timer.isSelected) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Selected",
                        modifier = Modifier
                            .size(80.dp)
                            .scale(2.0f),
                        tint = Color(0xFF00E676)
                    )
                }
            }
            if (timer.isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0x1A6C5CE7),
                                    Color.Transparent
                                ),
                                radius = 200f
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                )
            }
        }
    }
}
