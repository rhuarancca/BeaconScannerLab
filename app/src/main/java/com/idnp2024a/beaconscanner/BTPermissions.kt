package com.idnp2024a.beaconscanner.permissions

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.idnp2024a.beaconscanner.MainActivityBLE

class BTPermissions(private val activity: MainActivityBLE) {
    private val TAG = "BTPermissions"
    private var permissionsList: ArrayList<String> = ArrayList()
    private lateinit var alertDialog: AlertDialog

    private val permissions = arrayOf(
        android.Manifest.permission.BLUETOOTH_CONNECT,
        android.Manifest.permission.BLUETOOTH_SCAN,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.BLUETOOTH_ADMIN,
        android.Manifest.permission.BLUETOOTH_ADVERTISE,
    )

    private var permissionsLauncher: ActivityResultLauncher<Array<String>> =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            handlePermissionsResult(result)
        }

    fun checkPermissions() {
        permissionsList.clear()
        permissions.forEach {
            if (!hasPermission(activity, it)) {
                permissionsList.add(it)
            }
        }
        if (permissionsList.isNotEmpty()) {
            askForPermissions(permissionsList)
        } else {
            Log.d(TAG, "All permissions are already granted!")
        }
    }

    private fun handlePermissionsResult(result: Map<String, Boolean>) {
        val deniedPermissions = result.filter { !it.value }.keys
        val permissionsToRequest = ArrayList<String>()
        var permanentlyDenied = false

        deniedPermissions.forEach { permission ->
            if (activity.shouldShowRequestPermissionRationale(permission)) {
                permissionsToRequest.add(permission)
            } else if (!hasPermission(activity, permission)) {
                permanentlyDenied = true
            }
        }

        when {
            permissionsToRequest.isNotEmpty() -> askForPermissions(permissionsToRequest)
            permanentlyDenied -> showPermissionDialog()
            else -> Log.d(TAG, "All permissions are granted!")
        }
    }

    private fun hasPermission(context: Context, permissionStr: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permissionStr) == PackageManager.PERMISSION_GRANTED
    }

    private fun askForPermissions(permissions: List<String>) {
        permissionsLauncher.launch(permissions.toTypedArray())
    }

    private fun showPermissionDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle("Permission required")
            .setMessage("Some permissions need to be allowed to use this app without any problems.")
            .setPositiveButton("Settings") { dialog, _ ->
                dialog.dismiss()
                // Implement redirection to app settings if needed
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        if (!::alertDialog.isInitialized) {
            alertDialog = builder.create()
        }
        if (!alertDialog.isShowing) {
            alertDialog.show()
        }
    }
}
