package com.iron.library

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView

/**
 * @author 최철훈
 * @created 2022-05-16
 * @desc CircleImageView
 */
class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    private val shaderMatrix: Matrix = Matrix()
    private var bitmap: Bitmap? = null
    private var bitmapShader: Shader? = null
    private val bitmapPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmapDrawBounds: RectF = RectF()

    init {
        setBitmap()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.run { drawOval(bitmapDrawBounds, bitmapPaint) }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        setBitmapDrawBounds(bitmapDrawBounds)
        resizeBitmap()
    }

    private fun setBitmap() {
        bitmap = getBitmapFromDrawable(drawable) ?: return
        bitmapShader = BitmapShader(bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        bitmapPaint.shader = bitmapShader

        resizeBitmap()
    }

    private fun setBitmapDrawBounds(bounds: RectF) { //ImageView 가로/세로 길이에 따라 적절한 가운데 위치 설정
        val calculatedWidth = (width - paddingLeft - paddingRight).toFloat()
        val calculatedHeight = (height - paddingTop - paddingBottom).toFloat()
        var calculatedLeft = paddingLeft.toFloat()
        var calculatedTop = paddingTop.toFloat()

        if (calculatedWidth > calculatedHeight) {
            calculatedLeft += (calculatedWidth - calculatedHeight) / 2
        } else {
            calculatedTop += (calculatedHeight - calculatedWidth) / 2
        }

        val diameter = calculatedWidth.coerceAtMost(calculatedHeight)
        bounds.set(calculatedLeft, calculatedTop, calculatedLeft + diameter, calculatedTop + diameter)
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    private fun resizeBitmap() {
        if (bitmap == null) return

        val bitmapWidth = bitmap?.width ?: return
        val bitmapHeight = bitmap?.height ?: return
        val bitmapBoundsWidth = bitmapDrawBounds.width()
        val bitmapBoundsHeight = bitmapDrawBounds.height()
        val scale: Float
        val dx: Float
        val dy: Float

        if (bitmapWidth < bitmapHeight) {
            scale = (bitmapBoundsWidth / bitmapWidth)
            dx = bitmapDrawBounds.left
            dy = bitmapDrawBounds.top - (bitmapHeight * scale / 2) + (bitmapBoundsWidth / 2)
        } else {
            scale = (bitmapBoundsHeight / bitmapHeight)
            dx = bitmapDrawBounds.left - (bitmapWidth * scale / 2) + (bitmapBoundsWidth / 2)
            dy = bitmapDrawBounds.top
        }

        shaderMatrix.apply {
            setScale(scale, scale)
            postTranslate(dx, dy)
        }.also { matrix ->
            bitmapShader!!.setLocalMatrix(matrix)
        }
    }
}