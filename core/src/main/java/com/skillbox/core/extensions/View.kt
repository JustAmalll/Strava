package com.skillbox.core.extensions

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import java.math.BigDecimal

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)


fun Context.getDimension(value: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value, this.resources.displayMetrics
    ).toInt()
}

fun Float.roundTo2DecimalPlaces() =
    BigDecimal(this.toDouble()).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()