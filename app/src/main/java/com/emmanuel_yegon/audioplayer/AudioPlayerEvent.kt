package com.emmanuel_yegon.audioplayer

import android.content.Context
import com.emmanuel_yegon.audioplayer.domain.model.AudioPlayerMetaData

sealed class AudioPlayerEvent {

    data class InitAudio(
        val audio: AudioPlayerMetaData,
        val context: Context,
        val onAudioInitialized: () -> Unit,
    ) : AudioPlayerEvent()

    data class Seek(val position:Float): AudioPlayerEvent()

    data class LikeOrNot(val id: Long): AudioPlayerEvent()

    object Play: AudioPlayerEvent()

    object Pause: AudioPlayerEvent()

    object Stop: AudioPlayerEvent()

    object LoadMedias: AudioPlayerEvent()

    object HideLoadingDialog: AudioPlayerEvent()

}
