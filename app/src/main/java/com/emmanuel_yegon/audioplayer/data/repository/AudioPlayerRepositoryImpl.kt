package com.emmanuel_yegon.audioplayer.data.repository

import android.content.Context
import android.graphics.Bitmap

import android.net.Uri
import com.emmanuel_yegon.audioplayer.domain.model.AudioPlayerMetaData
import com.emmanuel_yegon.audioplayer.domain.repository.AudioPlayerRepository
import com.emmanuel_yegon.audioplayer.util.audio.MetaDataHelper
import com.emmanuel_yegon.audioplayer.util.audio.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AudioPlayerRepositoryImpl @Inject constructor(
    private val metaDataHelper: MetaDataHelper,
    private val userPreferences: UserPreferences
): AudioPlayerRepository {

    override suspend fun getAudios(): List<AudioPlayerMetaData> {
        return withContext(Dispatchers.IO){
            metaDataHelper.getAudios()
        }
    }

    override suspend fun loadCoverBitmap(context: Context, uri: Uri): Bitmap? {
        return withContext(Dispatchers.IO){
            metaDataHelper.getAlbumArt(
                context=context,
                uri=uri
            )
        }
    }

    override suspend fun likeOrNot(id: Long) {
        withContext(Dispatchers.IO){
            userPreferences.likeOrNot(id=id)
        }
    }

    override fun getFavoriteSongs(): Flow<List<Long>> {
        return userPreferences.favoriteSongs
    }



}