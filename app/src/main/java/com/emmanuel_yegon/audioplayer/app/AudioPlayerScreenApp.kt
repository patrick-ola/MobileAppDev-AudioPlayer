package com.emmanuel_yegon.audioplayer.app

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.emmanuel_yegon.audioplayer.MainViewModel
import com.emmanuel_yegon.audioplayer.navigation.AudioPlayerRouter
import com.emmanuel_yegon.audioplayer.navigation.Screen
import com.emmanuel_yegon.audioplayer.screen.Login
import com.emmanuel_yegon.audioplayer.screen.Music
import com.emmanuel_yegon.audioplayer.screen.SignUp
import com.emmanuel_yegon.audioplayer.screen.TermsAndConditions


@Composable
fun AudioPlayerApp(mainViewModel: MainViewModel){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Crossfade(targetState = AudioPlayerRouter.currentScreen, label = "") { currentState->
            when(currentState.value){
                is Screen.SignUp -> {
                    SignUp()
                }
                is Screen.TermsAndConditions -> {
                    TermsAndConditions()
                }
                is Screen.Login -> {
                    Login()
                }
                is Screen.Music -> {
                    Music(mainViewModel = mainViewModel)
                }

            }
        }
    }
}