package com.iron.library

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.iron.util.ifLet

/**
 * @author iron.choi
 * @created 2022-04-06
 * @desc
 */
class CustomSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatSeekBar(context, attrs, defStyle) {

    private var isTouched = false

    private var mLinePaint: Paint? = null
    private var mProgressPaint: Paint? = null
    private var mThumbPaint: Paint? = null
    private var mTextPaint: Paint? = null

    private var mUnselectTickMark: Drawable? = null
    private var mSelectTickMark: Drawable? = null

    private var mThumbMarkFirst: Bitmap? = null
    private var mThumbMarkSecond: Bitmap? = null
    private var mThumbMarkThird: Bitmap? = null

    init {
        setLinePaint()
        setProgressPaint()
        setThumbPaint()
        setTextPaint()
        setThumbMark()
        setTickMark()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.run(::setCustomDraw)
    }

    private fun setCustomDraw(canvas: Canvas) {
        val rangeCount = BUTTON_RANGE_COUNT
        val tickMarkSpacing: Float = (width - paddingStart - paddingEnd) / rangeCount.toFloat()
        val progressSpacing: Float = (width - paddingStart - paddingEnd) / max.toFloat()
        val progressY = height / BUTTON_RANGE_COUNT * 3
        val thumbY = height / 16 * 1
        var tickMarkHalfWidth = 0

        ifLet(canvas, mLinePaint, mProgressPaint) { (canvas, linePaint, progressPaint) ->
            (canvas as Canvas).apply {
                drawLine(
                    paddingStart.toFloat(),
                    progressY.toFloat(),
                    (width - paddingEnd).toFloat(),
                    progressY.toFloat(),
                    linePaint as Paint
                )
                drawLine(
                    paddingStart.toFloat(),
                    progressY.toFloat(),
                    paddingStart.toFloat() + progressSpacing * progress,
                    progressY.toFloat(),
                    progressPaint as Paint
                )
            }
        }

        ifLet(mUnselectTickMark, mSelectTickMark) { (unSelectTickMark, selectTickMark) ->
            var tickMarkWidth: Int = unSelectTickMark.intrinsicWidth
            var tickMarkHeight: Int = unSelectTickMark.intrinsicHeight
            var halfTickMarkWidth = if (tickMarkWidth >= 0) tickMarkWidth / 2 else 1
            var halfTickMarkHeight = if (tickMarkHeight >= 0) tickMarkHeight / 2 else 1
            unSelectTickMark.setBounds(-halfTickMarkWidth, -halfTickMarkHeight, halfTickMarkWidth, halfTickMarkHeight)
            tickMarkHalfWidth = halfTickMarkWidth
            var saveCount: Int = canvas.save()
            canvas.translate(paddingStart.toFloat(), progressY.toFloat())

            for (i in 0..rangeCount) {
                unSelectTickMark.draw(canvas)
                canvas.translate(tickMarkSpacing, 0f)
            }

            canvas.restoreToCount(saveCount)

            tickMarkWidth = selectTickMark.intrinsicWidth
            tickMarkHeight = selectTickMark.intrinsicHeight
            halfTickMarkWidth = if (tickMarkWidth >= 0) tickMarkWidth / 2 else 1
            halfTickMarkHeight = if (tickMarkHeight >= 0) tickMarkHeight / 2 else 1
            selectTickMark.setBounds(-halfTickMarkWidth, -halfTickMarkHeight, halfTickMarkWidth, halfTickMarkHeight)

            val choiceCount = progress / 5
            saveCount = canvas.save()
            canvas.translate(paddingStart.toFloat(), progressY.toFloat())

            for (i in 0..choiceCount) {
                selectTickMark.draw(canvas)
                canvas.translate(tickMarkSpacing, 0f)
            }

            canvas.restoreToCount(saveCount)
        }

        ifLet(mThumbMarkFirst, mThumbMarkSecond, mThumbMarkThird, mThumbPaint, mTextPaint) { (thumbMarkFirst, thumbMarkSecond, thumbMarkThird, thumbPaint, textPaint) ->
            val mX = (thumbMarkFirst as Bitmap).width
            if(isTouched) {
                when(progress) {
                    PROGRESS_FIRST -> canvas.drawBitmap(thumbMarkFirst, paddingStart.toFloat() - tickMarkHalfWidth + (progressSpacing * progress), thumbY.toFloat(), thumbPaint as Paint)
                    PROGRESS_LAST -> canvas.drawBitmap(thumbMarkThird as Bitmap, -paddingEnd.toFloat() - tickMarkHalfWidth + (progressSpacing * progress), thumbY.toFloat(), thumbPaint as Paint)
                    else -> canvas.drawBitmap(thumbMarkSecond as Bitmap, paddingStart.toFloat() - (mX / 2) + (progressSpacing * progress), thumbY.toFloat(), thumbPaint as Paint)
                }
            } else {
                clearThumbMark(canvas)
                setThumbMark()
            }

            val progressText = "${progress * 5}%"
            val progressTextY = thumbY.toFloat() * 5 + 3
            val extraPadding = when(progressText.length) {
                TEXT_SIZE_ONES -> 4
                TEXT_SIZE_HUNDREDS -> 7
                else -> 0
            }
            when(progress) {
                PROGRESS_FIRST -> canvas.drawText(progressText, paddingStart + extraPadding + (progressSpacing * progress), progressTextY, textPaint as Paint)
                PROGRESS_LAST -> canvas.drawText(progressText, (progressSpacing * progress) - extraPadding - paddingEnd, progressTextY, textPaint as Paint)
                else -> canvas.drawText(progressText, (progressSpacing * progress) + extraPadding, progressTextY, textPaint as Paint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            isTouched = when(it.action) {
                MotionEvent.ACTION_UP -> {
                    false
                }
                else -> {
                    true
                }
            }
        }

        return super.onTouchEvent(event)
    }

    private fun setLinePaint() {
        mLinePaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            color = Color.parseColor("#F8FAFC")
            strokeWidth = 20f
        }
    }

    private fun setProgressPaint() {
        mProgressPaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            color = Color.parseColor("#FDC6CE")
            strokeWidth = 20f
        }
    }

    private fun setThumbPaint() {
        mThumbPaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            color = Color.BLACK
            strokeWidth = 20f
        }
    }

    private fun setTextPaint() {
        mTextPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
            textSize = 28.0f
            color = Color.WHITE
        }
    }

    private fun setThumbMark() {
        mThumbMarkFirst = ContextCompat.getDrawable(context, R.drawable.ic_seekbar_thumb_start)?.toBitmap()
        mThumbMarkSecond = ContextCompat.getDrawable(context, R.drawable.ic_seekbar_thumb_center)?.toBitmap()
        mThumbMarkThird = ContextCompat.getDrawable(context, R.drawable.ic_seekbar_thumb_end)?.toBitmap()
    }

    private fun clearThumbMark(canvas: Canvas?) {
        canvas?.run {
            mThumbMarkFirst?.eraseColor(Color.TRANSPARENT)
            mThumbMarkSecond?.eraseColor(Color.TRANSPARENT)
            mThumbMarkThird?.eraseColor(Color.TRANSPARENT)

            drawBitmap(mThumbMarkFirst!!, 0f , 0f, null)
            drawBitmap(mThumbMarkSecond!!, 0f , 0f, null)
            drawBitmap(mThumbMarkThird!!, 0f , 0f, null)
        }
    }

    private fun setTickMark() {
        mUnselectTickMark = ContextCompat.getDrawable(context, R.drawable.ic_unselect_tickmark)
        mSelectTickMark = ContextCompat.getDrawable(context, R.drawable.ic_select_tickmark)
    }

    companion object {
        const val BUTTON_RANGE_COUNT = 4
        const val PROGRESS_FIRST = 0
        const val PROGRESS_LAST = 20
        const val TEXT_SIZE_ONES = 2
        const val TEXT_SIZE_HUNDREDS = 4
    }
}