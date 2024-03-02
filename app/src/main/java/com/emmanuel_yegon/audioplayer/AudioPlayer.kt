package com.emmanuel_yegon.audioplayer

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AudioPlayer:Application() {

    override fun onCreate(){
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}

