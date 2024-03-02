package com.emmanuel_yegon.audioplayer.domain.model

import android.graphics.Bitmap
import android.net.Uri

data class AudioPlayerMetaData (
    val contentUri: Uri,
    val songId: Long,
    val cover: Bitmap?,
    val songTitle: String,
    val artist:String,
    val duration: Int
){
    companion object{
        fun emptyMetaData(): AudioPlayerMetaData{
            return AudioPlayerMetaData(
                contentUri = Uri.EMPTY,
                songId = 0L,
                cover = null,
                songTitle = "",
                artist = "",
                duration = 0

            )
        }
    }
}