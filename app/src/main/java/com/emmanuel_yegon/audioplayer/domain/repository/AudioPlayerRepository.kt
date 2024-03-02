package com.emmanuel_yegon.audioplayer.domain.repository

import android.content.Context
import android.graphics.Bitmap

import android.net.Uri
import com.emmanuel_yegon.audioplayer.domain.model.AudioPlayerMetaData
import kotlinx.coroutines.flow.Flow

interface AudioPlayerRepository {

    suspend fun getAudios(): List<AudioPlayerMetaData>

    suspend fun loadCoverBitmap(context: Context, uri: Uri): Bitmap?

    suspend fun likeOrNot(id: Long)

    fun getFavoriteSongs(): Flow<List<Long>>
}