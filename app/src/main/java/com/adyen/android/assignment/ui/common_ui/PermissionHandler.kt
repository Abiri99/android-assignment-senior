package com.adyen.android.assignment.ui.common_ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

private const val TAG = "PermissionHandler"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionHandler(
    permission: String,
    onGranted: () -> Unit,
    onDegradeFunctionality: () -> Unit,
) {
    val context = LocalContext.current

    var showRationale by remember { mutableStateOf(false) }
    var requestPermission by remember { mutableStateOf(true) }

    val permissionState = rememberPermissionState(permission = permission)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            Log.d(TAG, "Permission result: $granted")
            if (granted) {
                onGranted()
            } else {
                if (showRationale) {
                    // already shown a rationale to the user but they haven't accepted
                    requestPermission = false
                    onDegradeFunctionality()
                } else {
                    if (permissionState.status.shouldShowRationale) {
                        showRationale = true
                    } else {
                        // degrade the app
                        onDegradeFunctionality()
                    }
                }
            }
        }
    )

    // Check and request permission
    LaunchedEffect(requestPermission) {
        if (requestPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            requestPermission = false
        }
    }

    // Show rationale dialog if needed
    if (showRationale) {
        AlertDialog(
            onDismissRequest = {
                // degrade the app
                onDegradeFunctionality()
            },
            title = { Text("Location Permission Required") },
            text = { Text("This app needs location permission to display your current location.") },
            confirmButton = {
                TextButton(onClick = {
                    showRationale = false
                    // go to the settings
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text("Allow")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    // degrade the app
                    showRationale = false

                    Toast.makeText(
                        context,
                        "Permission $permission denied",
                        Toast.LENGTH_SHORT
                    ).show()

                    onDegradeFunctionality()
                }) {
                    Text("Deny")
                }
            }
        )
    }
}