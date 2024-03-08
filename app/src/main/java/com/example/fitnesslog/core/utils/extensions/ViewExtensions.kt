package com.example.fitnesslog.core.utils.extensions

import android.os.SystemClock
import android.view.View


fun View.setThrottledOnClickListener(throttleTime: Long = 500L, action: (view: View) -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            // elapsedRealTime is monotonic from when system was booted, check if duration from last click > debounce time
            if (SystemClock.elapsedRealtime() - lastClickTime < throttleTime) {
                return
            }
            lastClickTime = SystemClock.elapsedRealtime()
            action(v)
        }
    })
}
