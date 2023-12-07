package com.example.fitnesslog.core.utils

import android.content.res.Resources

fun dpToPx(dp: Int, resources: Resources): Int {
    return (dp * resources.displayMetrics.density).toInt()
}