package com.example.eatout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TagLabel(
    text: String,
    isCompact: Boolean = false
) {
    val textStyle = if (isCompact) {
        MaterialTheme.typography.labelSmall
    } else {
        MaterialTheme.typography.titleSmall
    }

    val paddingHorizontal = if (isCompact) 8.dp else 14.dp
    val paddingVertical = if (isCompact) 4.dp else 8.dp

    Text(
        text = text,
        style = textStyle,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = paddingHorizontal, vertical = paddingVertical),
        color = MaterialTheme.colorScheme.onSecondaryContainer
    )
}