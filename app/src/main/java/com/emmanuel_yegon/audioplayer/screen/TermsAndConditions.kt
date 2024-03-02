package com.emmanuel_yegon.audioplayer.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emmanuel_yegon.audioplayer.R
import com.emmanuel_yegon.audioplayer.navigation.AudioPlayerRouter
import com.emmanuel_yegon.audioplayer.navigation.Screen

@Composable
fun TermsAndConditions() {
    val countryList =
        mutableListOf(
            stringResource(id = R.string.terms_and_conditions_header)+"\n",

            "1.Information Collected: We may collect personal information such as your name, email address, and device information for the purpose of providing and improving our app.",
            "2.Use of Information: Your personal information is used to personalize your experience, improve customer service.",
            "3.Data Security: We implement a variety of security measures to safeguard your personal information.",
        )

    val listModifier = Modifier
        .background(Color.White)
        .padding(end = 4.dp, start = 4.dp)

    val textStyle = TextStyle(fontSize = 24.sp, color = Color.Black)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, start = 16.dp, end = 16.dp, bottom = 50.dp)
    ) {

        LazyColumn(modifier = listModifier) {

            items(countryList) { country ->
                Text(text = country, style = textStyle)
            }
        }

        BackHandler {
            AudioPlayerRouter.navigateTo(Screen.SignUp)
        }
    }

}

@Preview
@Composable
fun TermsAndConditionsPreview() {
    TermsAndConditions()
}