package com.emmanuel_yegon.audioplayer

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmanuel_yegon.audioplayer.domain.model.AudioPlayerMetaData
import com.emmanuel_yegon.audioplayer.domain.repository.AudioPlayerRepository
import com.emmanuel_yegon.audioplayer.util.audio.VisualizerData
import com.emmanuel_yegon.audioplayer.util.audio.VisualizerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AudioPlayerRepository,
) : ViewModel() {

    private var _player: MediaPlayer? = null

    private var _visualizerHelper = VisualizerHelper()

    private var _state by mutableStateOf(value = AudioPlayerState())
    val state: AudioPlayerState
        get() = _state

    private val _visualizerData =
        mutableStateOf(value = VisualizerData.emptyVisualizerData())
    val visualizerData: State<VisualizerData>
        get() = _visualizerData

    private val _handler = Handler(Looper.getMainLooper())

    init {
        loadMedias()
    }

    fun onEvent(event: AudioPlayerEvent) {
        when (event) {

            is AudioPlayerEvent.InitAudio -> initAudio(
                audio = event.audio,
                context = event.context,
                onAudioInitialized = event.onAudioInitialized
            )

            is AudioPlayerEvent.Seek -> seek(position = event.position)

            is AudioPlayerEvent.LikeOrNot -> likeOrNot(id=event.id)

            AudioPlayerEvent.LoadMedias -> loadMedias()
            AudioPlayerEvent.Pause -> pause()
            AudioPlayerEvent.Play -> play()
            AudioPlayerEvent.Stop -> stop()
            AudioPlayerEvent.HideLoadingDialog -> hideLoadingDialog()

        }
    }

    private fun loadMedias() {
        viewModelScope.launch {
            _state = _state.copy(isLoading = true)
            val audios = mutableListOf<AudioPlayerMetaData>()
            audios.addAll(prepareAudios())

            _state = _state.copy(audios = audios)
            repository.getFavoriteSongs().collect{favoriteSongs ->
                _state = _state.copy(
                    favoriteSongs=favoriteSongs,
                    isLoading = false
                )
            }

        }
    }


    private suspend fun prepareAudios(): List<AudioPlayerMetaData> {
        return repository.getAudios().map { audio ->
            val artist = if (audio.artist.contains("<unknown>"))
                "Unknown artist" else audio.artist
            audio.copy(artist = artist)
        }
    }

    private fun initAudio(audio: AudioPlayerMetaData, context: Context, onAudioInitialized: () -> Unit) {
        viewModelScope.launch {
            _state=_state.copy(isLoading = true)

            delay(800)

            val cover = repository.loadCoverBitmap(
                context = context,
                uri = audio.contentUri
            )

            _state=_state.copy(selectedAudio = audio.copy(cover = cover))

            _player = MediaPlayer().apply {
                setDataSource(context, audio.contentUri)
                prepare()
            }

            _player?.setOnCompletionListener {

                pause()
                playNextTrack(context)
            }

            _player?.setOnPreparedListener{
                onAudioInitialized()
            }
            _state=_state.copy(isLoading = false)

        }
    }

    private fun playNextTrack(context: Context) {
        val currentIndex = _state.audios.indexOf(_state.selectedAudio)

        // Determine the index of the next audio
        val nextIndex = (currentIndex + 1) % _state.audios.size

        // Get the next audio
        val nextAudio = _state.audios[nextIndex]

        stop()

        // Initialize and play the next audio
        initAudio(
            audio = nextAudio,
            context =context ,
            onAudioInitialized = {
                // Play the next audio
                play()

                // Optionally, you can hide the loading dialog or perform other actions
                hideLoadingDialog()
            }
        )
    }

    private fun play() {
        _state=_state.copy(isLoading = true)

        _player?.start()

        _player?.run {
            _visualizerHelper.start(
                audioSessionId = audioSessionId,
                onData = { data ->
                    _visualizerData.value = data
                }
            )
        }

        _handler.postDelayed(object : Runnable{
            override fun run(){
                try {
                    _state = _state.copy(currentPosition = _player!!.currentPosition)
                    _handler.postDelayed(this,1000)
                }catch (exp: Exception){
                    _state = _state.copy(currentPosition = 0)
                }
            }
        },0)

        _state = _state.copy(isLoading = false)
        _state=_state.copy(isPlaying = true)

    }

    private fun pause() {
        _state=_state.copy(isPlaying = false)
        _visualizerHelper.stop()
        _player?.pause()
    }

    private fun stop() {
        _visualizerHelper.stop()
        _player?.stop()
        _player?.reset()
        _player?.release()
        _state=_state.copy(
            isPlaying = false,
            currentPosition = 0
        )
        _player = null
    }

    private fun seek(position: Float) {
        _player?.run {
            seekTo(position.toInt())
        }
    }

   private fun hideLoadingDialog(){
       _state = _state.copy(isLoading = false)
   }

    private fun likeOrNot(id: Long){
        viewModelScope.launch {
            repository.likeOrNot(id=id)
        }
    }

}

