package com.emmanuel_yegon.audioplayer.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emmanuel_yegon.audioplayer.util.audio.millisecondsToTimeString

@Composable
fun TimeBar(
    currentPosition: Int,
    duration: Int,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
){
    Row(
        modifier=modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = millisecondsToTimeString(milliseconds = currentPosition),
            style = MaterialTheme.typography.bodyMedium,
            color=MaterialTheme.colorScheme.onSurface
        )
        Slider(
            modifier=Modifier.padding(horizontal = 8.dp)
                .fillMaxWidth(fraction = 0.7f),
            value = currentPosition.toFloat(),
            onValueChange = {value ->
                onValueChange(value)
            },
            valueRange=0f..duration.toFloat()
        )
        Text(
            text = millisecondsToTimeString(milliseconds = duration),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}