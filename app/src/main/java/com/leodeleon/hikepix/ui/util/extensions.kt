package com.leodeleon.hikepix.ui.util

import android.app.PendingIntent
import android.os.Build

fun pendingIntentFlags(flags: Int = 0): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        flags or PendingIntent.FLAG_IMMUTABLE
    } else flags
}