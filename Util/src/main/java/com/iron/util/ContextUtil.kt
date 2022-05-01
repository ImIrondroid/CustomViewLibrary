package com.iron.util

import android.content.Context
import android.util.TypedValue
import androidx.annotation.DimenRes

/**
 * @author 최철훈
 * @created 2022-04-21
 * @desc Util 클래스
 */
object ContextUtil {

    fun numberToPx(context: Context, dp: Int) =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(), context.resources.displayMetrics
        ).toInt()

    fun dpToPx(context: Context, @DimenRes id: Int) =
        context.resources.getDimensionPixelSize(id)

    fun pxToDp(context: Context, pixel: Int) =
        (pixel / context.resources.displayMetrics.density).toInt()
}