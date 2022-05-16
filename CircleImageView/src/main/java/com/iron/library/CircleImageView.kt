package com.iron.library

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
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

    private val mShaderMatrix: Matrix = Matrix()
    private var mBitmap: Bitmap? = null
    private var mBitmapShader: Shader? = null
    private val mBitmapPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBitmapDrawBounds: RectF = RectF()

    init {
        setBitmap()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.run { drawOval(mBitmapDrawBounds, mBitmapPaint) }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        setBitmapDrawBounds(mBitmapDrawBounds)
        resizeBitmap()
    }

    private fun setBitmap() {
        mBitmap = getBitmapFromDrawable(drawable) ?: return
        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mBitmapPaint.shader = mBitmapShader

        resizeBitmap()
    }

    private fun setBitmapDrawBounds(bounds: RectF) { //ImageView 가로 세로길이에 따라 적절한 위치(CENTER_VERTICAL|CENTER_HORIZONTAL) 설정하기
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
        if (mBitmap == null) return

        val bitmapWidth = mBitmap?.width ?: return
        val bitmapHeight = mBitmap?.height ?: return
        val bitmapBoundsWidth = mBitmapDrawBounds.width()
        val bitmapBoundsHeight = mBitmapDrawBounds.height()
        val scale: Float
        val dx: Float
        val dy: Float

        if (bitmapWidth < bitmapHeight) {
            scale = (bitmapBoundsWidth / bitmapWidth)
            dx = mBitmapDrawBounds.left
            dy = mBitmapDrawBounds.top - (bitmapHeight * scale / 2) + (bitmapBoundsWidth / 2)
        } else {
            scale = (bitmapBoundsHeight / bitmapHeight)
            dx = mBitmapDrawBounds.left - (bitmapWidth * scale / 2) + (bitmapBoundsWidth / 2)
            dy = mBitmapDrawBounds.top
        }

        mShaderMatrix.apply {
            setScale(scale, scale)
            postTranslate(dx, dy)
        }.also { matrix ->
            mBitmapShader!!.setLocalMatrix(matrix)
        }
    }
}