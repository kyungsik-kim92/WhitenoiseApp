package com.example.whitenoiseapp.core.ui.composables

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    shape: RoundedCornerShape = RoundedCornerShape(16.dp),
    selectedColor: Color = Color(0xFF26A69A),
    unselectedColor: Color = Color(0xFF37474F),
    elevation: Dp = 6.dp,
    selectedElevation: Dp = 12.dp,
    scaleAnimation: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected && scaleAnimation) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    val animatedElevation by animateDpAsState(
        targetValue = if (isSelected) selectedElevation else elevation,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "elevation"
    )

    Card(
        modifier = modifier
            .scale(animatedScale)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = animatedElevation),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) selectedColor else unselectedColor
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    }
}