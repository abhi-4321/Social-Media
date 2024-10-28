package com.example.socialmedia.util

import android.os.SystemClock
import android.view.View

fun View.setThrottledClickListener(
    throttleTime: Long = 1000,
    onClick: (View) -> Unit
) {
    var lastClickTime = 0L

    setOnClickListener { view ->
        val currentTime = SystemClock.elapsedRealtime()

        if (currentTime - lastClickTime >= throttleTime) {
            lastClickTime = currentTime
            onClick(view)
        }
    }
}

// For making any view temporarily unclickable
fun View.disableTemporarily(duration: Long = 1000) {
    isClickable = false
    postDelayed({ isClickable = true }, duration)
}