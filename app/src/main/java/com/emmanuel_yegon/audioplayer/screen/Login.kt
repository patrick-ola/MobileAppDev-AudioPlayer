package com.emmanuel_yegon.audioplayer.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.emmanuel_yegon.audioplayer.R
import com.emmanuel_yegon.audioplayer.components.ButtonComponent
import com.emmanuel_yegon.audioplayer.components.ClickableLoginText
import com.emmanuel_yegon.audioplayer.components.DividerTextComponent
import com.emmanuel_yegon.audioplayer.components.HeadingTextComponent
import com.emmanuel_yegon.audioplayer.components.MyTextFieldComponent
import com.emmanuel_yegon.audioplayer.components.NormalTextComponent
import com.emmanuel_yegon.audioplayer.components.PasswordTextField
import com.emmanuel_yegon.audioplayer.components.UnderLinedTextComponent
import com.emmanuel_yegon.audioplayer.data.LoginUIEvent
import com.emmanuel_yegon.audioplayer.data.LoginViewModel
import com.emmanuel_yegon.audioplayer.navigation.AudioPlayerRouter
import com.emmanuel_yegon.audioplayer.navigation.Screen
import com.emmanuel_yegon.audioplayer.navigation.SystemBackButtonHandler

@Composable
fun Login(loginViewModel: LoginViewModel = viewModel()) {

    val showToastMessage = loginViewModel.showToastMessage.value
    showToastMessage?.let { message ->
        Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
        loginViewModel.showToastMessage.value = null // Reset the value after showing the Toast
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(top = 150.dp, start = 28.dp, end = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                NormalTextComponent(value = stringResource(id = R.string.login))
                HeadingTextComponent(value = stringResource(id = R.string.welcome_back))

                Spacer(modifier = Modifier.height(20.dp))

                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.email),
                    painterResource(id = R.drawable.email_24),
                    onTextSelected = {
                        loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                    },
                    errorStatus = loginViewModel.loginUIState.value.emailError
                    )
                PasswordTextField(
                    labelValue = stringResource(id = R.string.password),
                    painterResource(id = R.drawable.outline_lock),
                    onTextSelected = {
                        loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                    },
                    errorStatus = loginViewModel.loginUIState.value.passwordError
                )
                Spacer(modifier = Modifier.height(40.dp))


                ButtonComponent(
                    value = stringResource(id = R.string.login),
                    onButtonClicked = {
                        loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                    },
                    isEnabled = loginViewModel.allValidationPassed.value
                )

                Spacer(modifier = Modifier.height(40.dp))
                DividerTextComponent()

                ClickableLoginText(tryingToLogin = false, onTextSelected = {
                    AudioPlayerRouter.navigateTo(Screen.SignUp)
                })
            }
        }

        if (loginViewModel.loginInProcess.value){
            CircularProgressIndicator()
        }
    }

}

@Preview
@Composable
fun LoginPreview() {
    Login()
}