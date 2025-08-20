package com.pmg.rickandmortylist_pablo_mata.utils.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GradientCapsuleBox(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color,
    fontSize: TextUnit = 10.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    gradientColors: List<Color>,
    borderWidth: Dp = 1.5.dp,
    shape: Shape = MaterialTheme.shapes.small
) {
    Box(
        modifier = modifier
            .border(
                width = borderWidth,
                brush = Brush.horizontalGradient(colors = gradientColors),
                shape = shape
            )
            .clip(shape)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = TextAlign.Center,
            modifier = modifier
        )
    }
}