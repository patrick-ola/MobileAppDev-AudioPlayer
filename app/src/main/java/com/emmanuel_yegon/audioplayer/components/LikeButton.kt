package com.emmanuel_yegon.audioplayer.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.emmanuel_yegon.audioplayer.R
import com.emmanuel_yegon.audioplayer.ui.theme.Pink500

@Composable
fun LikeButton(
    isFavorite: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonSize: Dp = 26.dp,
) {

    val transition = updateTransition(targetState = isFavorite, label = "transition")

    val animatedSize by transition.animateDp(
        transitionSpec = {
            keyframes {
                durationMillis = 500
                buttonSize + 10.dp at 250 using LinearOutSlowInEasing
                buttonSize at 400 using LinearOutSlowInEasing
            }
        },
        label = "animatedSize",
        targetValueByState = { state ->
            if (state) buttonSize else buttonSize
        }
    )

    val shouldBeAnimated = remember { mutableStateOf(value = false) }

    Icon(
        painter = painterResource(id = if (isFavorite) R.drawable.heart_solid else R.drawable.heart_outlined),
        contentDescription = "",
        tint = if (enabled) Pink500 else MaterialTheme.colorScheme.onSurface,
        modifier = modifier
            .clip(shape = RoundedCornerShape(size = 100.dp))
            .padding(all = 8.dp)
            .clickable(
                enabled = enabled,
                indication = rememberRipple(bounded = false),
                interactionSource = remember { MutableInteractionSource() },
                role = Role.Button,
                onClick = {
                    shouldBeAnimated.value = true
                    onClick()
                }
            )
            .size(if (shouldBeAnimated.value) animatedSize else buttonSize)
    )
}
