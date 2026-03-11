package com.empiriact.app.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object ActivityRecognitionPermissionUtils {
    fun isPermissionRequired(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    fun hasPermission(context: Context): Boolean {
        if (!isPermissionRequired()) {
            return true
        }

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED
    }
}

