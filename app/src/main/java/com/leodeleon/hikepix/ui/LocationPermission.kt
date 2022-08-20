package com.leodeleon.hikepix.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.MultiplePermissionsState
import com.leodeleon.hikepix.R
import splitties.resources.appStr

@Composable
fun LocationPermission(
    locationPermissionsState: MultiplePermissionsState,
    content: @Composable () -> Unit) {
    var permissionRequested by rememberSaveable { mutableStateOf(false) }

    if (locationPermissionsState.allPermissionsGranted) {
        content()
    } else {
        Column {
            val allPermissionsRevoked =
                locationPermissionsState.permissions.size ==
                        locationPermissionsState.revokedPermissions.size

            val textToShow = if (!allPermissionsRevoked) {
                // If not all the permissions are revoked, it's because the user accepted the COARSE
                // location permission, but not the FINE one.
                appStr(R.string.message_permission_revoked)
            } else if (locationPermissionsState.shouldShowRationale) {
                // Both location permissions have been denied
                appStr(R.string.message_permission_rationale)
            } else {
                // First time the user sees this feature or the user doesn't want to be asked again
                appStr(R.string.message_permission)
            }

            val userDenied = permissionRequested && !locationPermissionsState.allPermissionsGranted

            val buttonText = if (!allPermissionsRevoked) {
                appStr(R.string.allow_precise_location)
            } else if (userDenied){
                appStr(R.string.open_settings)
            } else {
                appStr(R.string.request_permission)
            }

            Text(text = textToShow)
            Spacer(modifier = Modifier.height(8.dp))

            if (userDenied) {
                val context = LocalContext.current
                    Button(onClick = {
                         openPermissionsSettings(context)
                    }) {
                        Text(appStr(R.string.open_settings))
                    }
            } else {
                Button(onClick = {
                    locationPermissionsState.launchMultiplePermissionRequest()
                    permissionRequested = true
                }) {
                    Text(buttonText)
                }
            }
        }
    }
}

private fun openPermissionsSettings(context: Context) {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
        it.data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(it)
    }
}