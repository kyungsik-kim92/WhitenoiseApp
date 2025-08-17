package com.example.whitenoiseapp.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.headlineMedium,
    fontWeight: FontWeight = FontWeight.Light
) {
    Text(
        text = text,
        style = style,
        color = color,
        fontWeight = fontWeight,
        modifier = modifier.padding(bottom = 20.dp)
    )
}