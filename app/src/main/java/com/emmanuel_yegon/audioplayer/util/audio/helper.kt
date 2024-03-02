package com.emmanuel_yegon.audioplayer.util.audio

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.emmanuel_yegon.audioplayer.domain.model.AudioPlayerMetaData


fun setUpPermissions(
    context: Context,
    permissions: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    onPermissionsGranted: (() -> Unit)? = null,
) {
    if (permissions.all { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }) {
        onPermissionsGranted?.invoke()
    } else {
        launcher.launch(permissions)
    }
}


fun showPermissionsRationalDialog(
    context: Context,
    dialogText: String,
    errorText: String,
    @StringRes okButtonTextResId: Int,
    @StringRes cancelButtonTextResId: Int,
    packageName: String,
) {
    AlertDialog.Builder(context).setMessage(dialogText)
        .setPositiveButton(okButtonTextResId) { _, _ ->
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                context.startActivity(intent)
            } catch (exp: Exception) {
                AlertDialog.Builder(context).setMessage(errorText)
            }
        }.setNegativeButton(cancelButtonTextResId) { dialog, _ ->
            dialog.dismiss()
        }.show()
}

fun AudioPlayerMetaData.isNotEmpty(): Boolean {
    return this != AudioPlayerMetaData.emptyMetaData()
}

@Composable
fun screenHeight(): Dp {
    return LocalContext.current.resources.displayMetrics.heightPixels.dp /
            LocalDensity.current.density
}


fun millisecondsToTimeString(milliseconds: Int): String {
    var result = ""
    var secondsString=""
    val hours = milliseconds / (1000 * 60 * 60)
    val minutes = (milliseconds % (1000 * 60 * 60)) / (1000 * 60)
    val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000)
    if (hours>0){
        result="$hours:"
    }
    secondsString=if(seconds < 10 ){
        "0$seconds"
    }else{
        "$seconds"
    }
    result = "$result$minutes:$secondsString"
    return result
}