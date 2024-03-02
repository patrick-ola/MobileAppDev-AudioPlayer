package com.emmanuel_yegon.audioplayer.data

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.emmanuel_yegon.audioplayer.data.rules.Validator
import com.emmanuel_yegon.audioplayer.navigation.AudioPlayerRouter
import com.emmanuel_yegon.audioplayer.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel :ViewModel() {

    private val TAG = LoginViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())

    var allValidationPassed = mutableStateOf(false)

    var loginInProcess = mutableStateOf(false)

    var showToastMessage: MutableState<String?> = mutableStateOf(null)



    fun onEvent(event:LoginUIEvent){
        when(event){
            is LoginUIEvent.EmailChanged ->{
                loginUIState.value = loginUIState.value.copy(
                    email = event.email
                )
            }

            is LoginUIEvent.PasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
            }

            is LoginUIEvent.LoginButtonClicked -> {
                login()
            }
        }
        validateLoginUIDataWithRules()
    }

    private fun login() {

        loginInProcess.value=true

        val email = loginUIState.value.email
        val password = loginUIState.value.password

        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                Log.d(TAG, "Inside_login_success")
                Log.d(TAG,"${it.isSuccessful}")

                loginInProcess.value=false

                if (it.isSuccessful){
                    AudioPlayerRouter.navigateTo(Screen.Music)
                }

            }
            .addOnFailureListener{
                Log.d(TAG, "Inside_login_failure")
                Log.d(TAG,"${it.localizedMessage}")
                loginInProcess.value=false
                showToastMessage.value = "Incorrect Username or Password"
            }
    }

    private fun validateLoginUIDataWithRules(){

        val  emailResult = Validator.validateEmail(
            email=loginUIState.value.email
        )

        val passwordResult = Validator.validatePassword(
            password = loginUIState.value.password
        )

        //update login UI state globally
        loginUIState.value=loginUIState.value.copy(
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )

        allValidationPassed.value = emailResult.status && passwordResult.status

    }
}