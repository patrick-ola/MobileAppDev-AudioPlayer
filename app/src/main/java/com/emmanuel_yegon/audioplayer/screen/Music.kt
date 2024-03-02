package com.emmanuel_yegon.audioplayer.screen

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.emmanuel_yegon.audioplayer.AudioPlayerEvent
import com.emmanuel_yegon.audioplayer.MainViewModel
import com.emmanuel_yegon.audioplayer.R
import com.emmanuel_yegon.audioplayer.components.FastButton
import com.emmanuel_yegon.audioplayer.components.LikeButton
import com.emmanuel_yegon.audioplayer.components.LoadingDialog
import com.emmanuel_yegon.audioplayer.components.PlayPauseButton
import com.emmanuel_yegon.audioplayer.components.StackedBarVisualizer
import com.emmanuel_yegon.audioplayer.components.TimeBar
import com.emmanuel_yegon.audioplayer.components.TopBar
import com.emmanuel_yegon.audioplayer.components.Track
import com.emmanuel_yegon.audioplayer.components.WarningMessage
import com.emmanuel_yegon.audioplayer.navigation.AudioPlayerRouter
import com.emmanuel_yegon.audioplayer.navigation.Screen
import com.emmanuel_yegon.audioplayer.ui.theme.AudioPlayerTheme
import com.emmanuel_yegon.audioplayer.ui.theme.Black3
import com.emmanuel_yegon.audioplayer.ui.theme.Typography
import com.emmanuel_yegon.audioplayer.util.audio.FORWARD_BACKWARD_STEP
import com.emmanuel_yegon.audioplayer.util.audio.isNotEmpty
import com.emmanuel_yegon.audioplayer.util.audio.screenHeight
import com.emmanuel_yegon.audioplayer.util.audio.setUpPermissions
import com.emmanuel_yegon.audioplayer.util.audio.showPermissionsRationalDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Music(
    mainViewModel: MainViewModel,
) {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        val sheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        )
        val context = LocalContext.current
        val packageName = context.packageName

        val scope = rememberCoroutineScope()

        val state = mainViewModel.state

        val dialogText = stringResource(id = R.string.txt_permissions)

        val errorText = stringResource(id = R.string.txt_error_app_settings)

        val screenHeight = screenHeight()

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                val permissionsGranted = permissions.values.reduce { acc, next -> acc && next }
                if (!permissionsGranted) {
                    showPermissionsRationalDialog(
                        context = context,
                        okButtonTextResId = R.string.lbl_ok,
                        cancelButtonTextResId = R.string.lbl_cancel,
                        dialogText = dialogText,
                        errorText = errorText,
                        packageName = packageName
                    )
                }

            })

        AudioPlayerTheme {
            ModalBottomSheetLayout(
                sheetState = sheetState,
                sheetContent = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(state = rememberScrollState())
                            .padding(top = 8.dp)
                    ) {
                        if (state.audios.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                WarningMessage(
                                    text = stringResource(id = R.string.txt_no_media),
                                    iconResId = R.drawable.circle_info_solid,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(id = R.string.lbl_tracks),
                                    fontWeight = FontWeight.Bold,
                                    //style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(top = 12.dp, bottom = 3.dp),
                                    textDecoration = TextDecoration.Underline,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            state.audios.forEach { audio ->
                                Track(
                                    audio = audio,
                                    isPlaying = audio.songId == state.selectedAudio.songId,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 10.dp)
                                        .requiredHeight(height = 70.dp),
                                    onClick = {
                                        scope.launch {
                                            mainViewModel.onEvent(AudioPlayerEvent.Stop)
                                            sheetState.hide()
                                            mainViewModel.onEvent(event = AudioPlayerEvent.InitAudio(
                                                audio = it,
                                                context = context,
                                                onAudioInitialized = {
                                                    mainViewModel.onEvent(event = AudioPlayerEvent.Play)
                                                    mainViewModel.onEvent(event = AudioPlayerEvent.HideLoadingDialog)
                                                }
                                            ))
                                        }
                                    }
                                )
                                Divider(modifier = Modifier.padding(horizontal = 8.dp))
                            }
                        }
                    }
                },
                content = {
                    LoadingDialog(
                        isLoading = state.isLoading,
                        onDone = {
                            mainViewModel.onEvent(event = AudioPlayerEvent.HideLoadingDialog)
                        },
                        modifier = Modifier
                            .clip(shape = MaterialTheme.shapes.large)
                            .background(color = MaterialTheme.colorScheme.surface)
                            .requiredSize(size = 80.dp),

                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(state = rememberScrollState())
                            .background(color = MaterialTheme.colorScheme.background),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        TopBar(

                            modifier = Modifier
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                                .requiredHeight(height = 80.dp),
                            leadingIcon = {
                                LikeButton(
                                    isFavorite = state.favoriteSongs.contains(state.selectedAudio.songId),
                                    enabled = state.selectedAudio.isNotEmpty(),
                                    onClick = {
                                        mainViewModel.onEvent(
                                            event = AudioPlayerEvent.LikeOrNot(
                                                id = state.selectedAudio.songId
                                            )
                                        )
                                    }
                                )
                            },
                            title = {
                                if (state.selectedAudio.isNotEmpty()) {
                                    val artist = if (state.selectedAudio.artist.contains(
                                            "unknown",
                                            ignoreCase = true
                                        )
                                    )
                                        "" else "${state.selectedAudio.artist} - "

                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append(text = artist)
                                            }
                                            append(text = state.selectedAudio.songTitle)
                                        },
                                        color = MaterialTheme.colorScheme.onSurface,
                                        overflow = TextOverflow.Clip,
                                        style = Typography.titleSmall
                                    )
                                }
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        setUpPermissions(
                                            context = context,
                                            permissions = arrayOf(
                                                Manifest.permission.RECORD_AUDIO,
                                                Manifest.permission.READ_EXTERNAL_STORAGE
                                            ),
                                            launcher = launcher,
                                            onPermissionsGranted = {
                                                scope.launch {
                                                    if (state.audios.isEmpty()) {
                                                        mainViewModel.onEvent(event = AudioPlayerEvent.LoadMedias)
                                                    }
                                                    sheetState.show()
                                                }
                                            }
                                        )
                                    }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.up_right_from_square_solid),
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        )

                        Spacer(modifier = Modifier.requiredHeight(height = 16.dp))

                        state.selectedAudio.cover?.let { cover ->
                            Image(
                                bitmap = cover.asImageBitmap(),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .requiredHeight(height = screenHeight * 0.4f)
                                    .clip(shape = MaterialTheme.shapes.large)
                            )
                        }
                            ?: Box(
                                modifier = Modifier.requiredHeight(height = screenHeight * 0.4f),
                                contentAlignment = Alignment.Center
                            ) {
                                Card(
                                    elevation = 8.dp,
                                    shape = MaterialTheme.shapes.large,
                                    modifier = Modifier.fillMaxHeight(fraction = 0.5f)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.musical_note_music_svgrepo_com),
                                        modifier = Modifier.padding(
                                            top = 25.dp,
                                            bottom = 26.dp,
                                            start = 25.dp,
                                            end = 25.dp
                                        ),
                                        contentDescription = "",
                                        contentScale = ContentScale.FillHeight
                                    )
                                }
                            }

                        Spacer(modifier = Modifier.requiredHeight(height = 16.dp))

                        StackedBarVisualizer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .requiredHeight(height = 200.dp)
                                .padding(vertical = 4.dp, horizontal = 8.dp),
                            shape = MaterialTheme.shapes.large,
                            barColors = listOf(
                                Color(0xFF1BEBE9),
                                Color(0xFF39AFEA),
                                Color(0xFF0291D8)
                            ),
                            data = mainViewModel.visualizerData.value,
                            barCount = 32,
                            stackedBarBackgroundColor = if (isSystemInDarkTheme()) Black3 else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                        )

                        Spacer(modifier = Modifier.requiredHeight(height = 10.dp))

                        TimeBar(
                            currentPosition = state.currentPosition,
                            onValueChange = { position ->
                                mainViewModel.onEvent(event = AudioPlayerEvent.Seek(position = position))
                            },
                            duration = state.selectedAudio.duration,
                        )

                        Spacer(modifier = Modifier.requiredHeight(height = 10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 10.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            FastButton(
                                enabled = state.currentPosition > FORWARD_BACKWARD_STEP,
                                onClick = {
                                          mainViewModel.onEvent(event = AudioPlayerEvent.Seek(
                                              position = state.currentPosition - FORWARD_BACKWARD_STEP
                                          ))
                                },
                                iconResId = R.drawable.backward_solid
                            )

                            PlayPauseButton(
                                enabled = state.selectedAudio.isNotEmpty(),
                                isPlaying = state.isPlaying,
                                modifier = Modifier.padding(horizontal = 26.dp),
                                onPlay = {
                                    mainViewModel.onEvent(event = AudioPlayerEvent.Play)
                                },
                                onPause = {
                                    mainViewModel.onEvent(event = AudioPlayerEvent.Pause)
                                }
                            )

                            FastButton(
                                enabled = state.currentPosition < (state.selectedAudio.duration - FORWARD_BACKWARD_STEP),
                                onClick = {
                                       mainViewModel.onEvent(event = AudioPlayerEvent.Seek(
                                           position = state.currentPosition + FORWARD_BACKWARD_STEP
                                       ))
                                },
                                iconResId = R.drawable.forward_solid
                            )
                        }
                    }
                }
            )
        }

        BackHandler {
            AudioPlayerRouter.navigateTo(Screen.Login)
            mainViewModel.onEvent(event = AudioPlayerEvent.Stop)
        }
    }
}

