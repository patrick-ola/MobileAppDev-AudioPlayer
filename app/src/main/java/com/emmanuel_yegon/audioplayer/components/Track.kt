package com.emmanuel_yegon.audioplayer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.magnifier
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.emmanuel_yegon.audioplayer.R
import com.emmanuel_yegon.audioplayer.domain.model.AudioPlayerMetaData

@Composable
fun Track(
    audio: AudioPlayerMetaData,
    isPlaying: Boolean,
    onClick: (AudioPlayerMetaData) -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxSize()
            .clickable { onClick(audio) }
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row (
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = R.drawable.mp3_logo),
                contentDescription = "",
                modifier= Modifier
                    .size(size = 50.dp)
                    .padding(all = 3.dp),
                contentScale = ContentScale.Crop
            )
            Column (
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 10.dp)
            ){
                Text(
                    text =audio.songTitle,
                    modifier=Modifier.padding(bottom = 1.dp),
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Clip,
                    color=if (isPlaying)MaterialTheme.colors.primary
                    else MaterialTheme.colors.onBackground
                )
                Text(
                    text = audio.artist,
                    color = if(isPlaying) MaterialTheme.colors.primary
                    else MaterialTheme.colors.onBackground
                )
            }
        }
        if (isPlaying){
            Icon(
                painter = painterResource(id = R.drawable.chart_simple_solid),
                contentDescription ="",
                tint = MaterialTheme.colors.primaryVariant,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}