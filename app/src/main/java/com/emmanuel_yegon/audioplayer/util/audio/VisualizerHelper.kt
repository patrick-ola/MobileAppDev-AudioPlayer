package com.emmanuel_yegon.audioplayer.util.audio

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.audiofx.Visualizer
import android.net.Uri
import androidx.annotation.WorkerThread

class VisualizerHelper {

    companion object{
        val CAPTURE_SIZE = Visualizer.getCaptureSizeRange()[1]
        const val SAMPLING_INTERVAL = 100
    }

    private var visualizer: Visualizer? = null

    private fun visualizerCallBack(onData: (VisualizerData) -> Unit) =
        object : Visualizer.OnDataCaptureListener{

            var lastDataTimestamp: Long? = null

            override fun onWaveFormDataCapture(
                visualizer: Visualizer,
                waveform: ByteArray,
                samplingRate: Int,
            ) {
                val now = System.currentTimeMillis()
                val durationSinceLastData = lastDataTimestamp?.let { now - it } ?: 0
                if (lastDataTimestamp == null || durationSinceLastData > SAMPLING_INTERVAL){
                    onData(
                        VisualizerData(
                            rawWaveForm = waveform.clone(),
                            captureSize = CAPTURE_SIZE
                        )
                    )
                    lastDataTimestamp=now
                }
            }

            override fun onFftDataCapture(
                visualizer: Visualizer,
                fft: ByteArray,
                samplingRate: Int,
            ) {

            }

        }

    fun start(audioSessionId: Int=0, onData: (VisualizerData) -> Unit){
        stop()
        visualizer = Visualizer(audioSessionId).apply {
            enabled=false
            captureSize= CAPTURE_SIZE
            setDataCaptureListener(
                visualizerCallBack(onData),
                Visualizer.getMaxCaptureRate(),
                true,
                true
            )
            enabled=true
        }
    }

    fun stop(){
        visualizer?.release()
        visualizer=null
    }
}