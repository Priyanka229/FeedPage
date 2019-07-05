package com.example.feedpage

import android.content.Context
import android.util.TypedValue
import kotlin.math.roundToInt

fun convertDPToPixel(context: Context, dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).roundToInt()
}