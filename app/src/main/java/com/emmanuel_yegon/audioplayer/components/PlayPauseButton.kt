package com.emmanuel_yegon.audioplayer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.emmanuel_yegon.audioplayer.R
import com.emmanuel_yegon.audioplayer.ui.theme.ButtonGradient
import com.emmanuel_yegon.audioplayer.ui.theme.Gray300

@Composable
fun PlayPauseButton(
    enabled:Boolean,
    isPlaying: Boolean,
    onPlay:() -> Unit,
    onPause:() -> Unit,
    modifier: Modifier = Modifier,
){
    Box(modifier = modifier
        .clip(shape = RoundedCornerShape(size = 100.dp))
        .clickable(
            indication = rememberRipple(bounded = false),
            enabled = enabled,
            onClick = { if (isPlaying) onPause() else onPlay() },
            interactionSource = remember { MutableInteractionSource() },
            role = Role.Button
        )
        .background(brush = ButtonGradient),
        contentAlignment = Alignment.Center
    ){
        Icon(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            painter = painterResource(id = if (isPlaying) R.drawable.pause_solid
            else R.drawable.play_solid),
            contentDescription = stringResource(id = if (isPlaying) R.string.lbl_pause else R.string.lbl_play),
            tint = if (enabled) MaterialTheme.colorScheme.onPrimary
            else Gray300
        )
    }
}