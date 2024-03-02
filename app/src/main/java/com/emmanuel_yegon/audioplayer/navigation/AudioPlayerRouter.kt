package com.emmanuel_yegon.audioplayer.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen() {
    object SignUp : Screen()
    object TermsAndConditions : Screen()
    object Login: Screen()
    object Music: Screen()
}

object AudioPlayerRouter {
    val currentScreen: MutableState<Screen> = mutableStateOf(Screen.Login)

    fun navigateTo(destination:Screen){
        currentScreen.value=destination
    }
}