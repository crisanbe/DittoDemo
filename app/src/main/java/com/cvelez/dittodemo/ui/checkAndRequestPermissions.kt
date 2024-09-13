package com.cvelez.dittodemo.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cvelez.dittodemo.ui.Constans.PERMISSION_REQUEST_CODE

 fun checkAndRequestPermissions(context: Context) {
    val permissionsToRequest = mutableListOf<String>()

    // Permisos comunes a todas las versiones
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    // Permisos específicos para Android 12 y superior (API 31+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN)
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_ADVERTISE)
        }
    }

    // Solicitar permisos si faltan algunos
    if (permissionsToRequest.isNotEmpty()) {
        ActivityCompat.requestPermissions(Activity(), permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
    }
}


