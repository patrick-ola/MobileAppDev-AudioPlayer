package com.emmanuel_yegon.audioplayer.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable () -> Unit,
    title: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)?=null,
) {

    Box(modifier = modifier.fillMaxWidth().heightIn(max=50.dp)){
        Box(modifier = Modifier.align(alignment = Alignment.CenterStart)){
            leadingIcon()
        }

        Box(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .padding(top = 30.dp, start = 35.dp, end = 35.dp),
        ){
            title?.invoke()
        }

        Box(modifier = Modifier.align(alignment = Alignment.CenterEnd)){
            trailingIcon?.invoke()
        }

    }
}