package com.emmanuel_yegon.audioplayer

import com.emmanuel_yegon.audioplayer.domain.model.AudioPlayerMetaData


data class AudioPlayerState(
    val isLoading: Boolean = false,
    val audios: List<AudioPlayerMetaData> = emptyList(),
    val selectedAudio: AudioPlayerMetaData = AudioPlayerMetaData.emptyMetaData(),
    val isPlaying: Boolean = false,
    val currentPosition: Int =0,
    val favoriteSongs: List<Long> = emptyList()
)
