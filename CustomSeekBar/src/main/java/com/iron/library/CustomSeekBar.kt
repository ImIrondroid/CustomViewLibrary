package com.iron.library

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.iron.util.ContextUtil
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
    private var pointCount = DEFAULT_POINT_COUNT

    private var mWidth = ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_2000)
    private val mHeight = ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_275)
    private var mPaddingStart = ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_100)
    private var mPaddingEnd = ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_100)

    private var mLinePaint: Paint? = null
    private var mLineColor: Int = 0
    private var mProgressPaint: Paint? = null
    private var mProgressColor: Int = 0
    private var mThumbMarkTextPaint: Paint? = null
    private var mThumbMarkTextColor: Int = 0
    private var mUnselectTickMark: Drawable? = null
    private var mSelectTickMark: Drawable? = null
    private var mThumbMarkFirst: Bitmap? = null
    private var mThumbMarkSecond: Bitmap? = null
    private var mThumbMarkThird: Bitmap? = null

    init {
        setAttributeSet(attrs)

        setLinePaint()
        setProgressPaint()
        setTextPaint()
        setThumbMark()
        setTickMark()
    }

    private fun setAttributeSet(attrs: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomSeekBar)

        pointCount = attributes.getInt(R.styleable.CustomSeekBar_pointCount, DEFAULT_POINT_COUNT)
        mLineColor = attributes.getColor(R.styleable.CustomSeekBar_lineColor, Color.parseColor("#F8FAFC"))
        mProgressColor = attributes.getColor(R.styleable.CustomSeekBar_progressColor, Color.parseColor("#FDC6CE"))
        mThumbMarkTextColor = attributes.getColor(R.styleable.CustomSeekBar_thumbMarkTextColor, Color.WHITE)

        if(mPaddingStart < paddingStart) mPaddingStart = paddingStart
        if(mPaddingEnd < paddingEnd) mPaddingEnd = paddingEnd
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        if ((widthMode == MeasureSpec.EXACTLY) or (widthMode == MeasureSpec.AT_MOST)) {
            mWidth = MeasureSpec.getSize(widthMeasureSpec)
        } else {
            //MeasureSpec.UNSPECIFIED: 동적으로 추가된 경우 width 또는 constraint 없을시 디폴트 사이즈로 대체
        }

        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.run {
            drawProgressBar(this)
            drawTickMark(this)
            drawThumbMark(this)
        }
    }

    private fun drawProgressBar(canvas: Canvas) {
        val progressHeight = mHeight / 4 * 3
        val progressInterval: Float = (mWidth - mPaddingStart - mPaddingEnd) / max.toFloat()

        ifLet(mLinePaint, mProgressPaint) { (linePaint, progressPaint) ->
            canvas.apply {
                drawLine(
                    mPaddingStart.toFloat(),
                    progressHeight.toFloat(),
                    (mWidth - mPaddingEnd).toFloat(),
                    progressHeight.toFloat(),
                    linePaint as Paint
                )
                drawLine(
                    mPaddingStart.toFloat(),
                    progressHeight.toFloat(),
                    mPaddingStart + progressInterval * progress,
                    progressHeight.toFloat(),
                    progressPaint as Paint
                )
            }
        }
    }

    private fun drawTickMark(canvas: Canvas) {
        val pointRangeCount = pointCount - 1
        val tickMarkInterval: Float = (mWidth - mPaddingStart - mPaddingEnd) / pointRangeCount.toFloat()
        val progressHeight = mHeight / 4 * 3

        ifLet(mUnselectTickMark, mSelectTickMark) { (unSelectTickMark, selectTickMark) ->
            var tickMarkWidth: Int = unSelectTickMark.intrinsicWidth
            var tickMarkHeight: Int = unSelectTickMark.intrinsicHeight
            var halfTickMarkWidth =
                if (tickMarkWidth != -1) tickMarkWidth / 2
                else ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_50)
            var halfTickMarkHeight =
                if (tickMarkHeight != -1) tickMarkHeight / 2
                else ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_50)
            unSelectTickMark.setBounds(
                -halfTickMarkWidth,
                -halfTickMarkHeight,
                halfTickMarkWidth,
                halfTickMarkHeight
            )

            var saveCount: Int = canvas.save()
            canvas.translate(mPaddingStart.toFloat(), progressHeight.toFloat())
            for (i in 0..pointRangeCount) {
                unSelectTickMark.draw(canvas)
                canvas.translate(tickMarkInterval, 0f)
            }
            canvas.restoreToCount(saveCount)

            tickMarkWidth = selectTickMark.intrinsicWidth
            tickMarkHeight = selectTickMark.intrinsicHeight
            halfTickMarkWidth =
                if (tickMarkWidth != -1) tickMarkWidth / 2
                else ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_50)
            halfTickMarkHeight =
                if (tickMarkHeight != -1) tickMarkHeight / 2
                else ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_50)
            selectTickMark.setBounds(
                -halfTickMarkWidth,
                -halfTickMarkHeight,
                halfTickMarkWidth,
                halfTickMarkHeight
            )

            val choiceCount = progress / pointCount
            saveCount = canvas.save()
            canvas.translate(mPaddingStart.toFloat(), progressHeight.toFloat())

            for (i in 0..choiceCount) {
                selectTickMark.draw(canvas)
                canvas.translate(tickMarkInterval, 0f)
            }

            canvas.restoreToCount(saveCount)
        }
    }

    private fun drawThumbMark(canvas: Canvas) {
        val progressInterval: Float = (mWidth - mPaddingStart - mPaddingEnd) / max.toFloat()
        val thumbHeight = mHeight / 16 * 1

        ifLet(mThumbMarkFirst, mThumbMarkSecond, mThumbMarkThird, mThumbMarkTextPaint) { (thumbMarkFirst, thumbMarkSecond, thumbMarkThird, textPaint) ->
            if (isTouched) {
                when (progress) {
                    0 -> canvas.drawBitmap(
                        thumbMarkFirst as Bitmap,
                        (mPaddingStart / 2).toFloat(),
                        thumbHeight.toFloat(),
                        null
                    )
                    max -> canvas.drawBitmap(
                        thumbMarkThird as Bitmap,
                        -(mPaddingEnd / 2).toFloat() + (progressInterval * progress),
                        thumbHeight.toFloat(),
                        null
                    )
                    else -> canvas.drawBitmap(
                        thumbMarkSecond as Bitmap,
                        //mPaddingStart.toFloat() - (mX / 2) + (progressInterval * progress),
                        progressInterval * progress,
                        thumbHeight.toFloat(),
                        null
                    )
                }

                val progressText = "${progress * 5}%"
                val progressTextY = thumbHeight.toFloat() * 5 + 3
                val extraPadding = when (progressText.length) {
                    TEXT_SIZE_ONES -> 4
                    TEXT_SIZE_HUNDREDS -> 7
                    else -> 0
                }

                when (progress) {
                    0 -> canvas.drawText(
                        progressText,
                        mPaddingStart + extraPadding + (progressInterval * progress),
                        progressTextY,
                        textPaint as Paint
                    )
                    max -> canvas.drawText(
                        progressText,
                        (progressInterval * progress) - extraPadding - mPaddingEnd,
                        progressTextY,
                        textPaint as Paint
                    )
                    else -> canvas.drawText(
                        progressText,
                        (progressInterval * progress) + extraPadding,
                        progressTextY,
                        textPaint as Paint
                    )
                }
            } else {
                clearThumbMark(canvas)
                setThumbMark()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            isTouched = when (it.action) {
                MotionEvent.ACTION_UP -> false
                else -> true
            }
        }

        return super.onTouchEvent(event)
    }

    private fun setLinePaint() {
        mLinePaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            color = mLineColor
            strokeWidth = 20f
        }
    }

    private fun setProgressPaint() {
        mProgressPaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            color = mProgressColor
            strokeWidth = 20f
        }
    }

    private fun setTextPaint() {
        mThumbMarkTextPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
            textSize = 28.0f
            color = mThumbMarkTextColor
        }
    }

    private fun setThumbMark() {
        mThumbMarkFirst =
            ContextCompat.getDrawable(context, R.drawable.ic_seekbar_thumb_start)?.toBitmap()
        mThumbMarkSecond =
            ContextCompat.getDrawable(context, R.drawable.ic_seekbar_thumb_center)?.toBitmap()
        mThumbMarkThird =
            ContextCompat.getDrawable(context, R.drawable.ic_seekbar_thumb_end)?.toBitmap()
    }

    private fun clearThumbMark(canvas: Canvas?) {
        canvas?.run {
            mThumbMarkFirst?.eraseColor(Color.TRANSPARENT)
            mThumbMarkSecond?.eraseColor(Color.TRANSPARENT)
            mThumbMarkThird?.eraseColor(Color.TRANSPARENT)

            drawBitmap(mThumbMarkFirst!!, 0f, 0f, null)
            drawBitmap(mThumbMarkSecond!!, 0f, 0f, null)
            drawBitmap(mThumbMarkThird!!, 0f, 0f, null)
        }
    }

    private fun setTickMark() {
        mUnselectTickMark = ContextCompat.getDrawable(context, R.drawable.ic_unselect_tickmark)
        mSelectTickMark = ContextCompat.getDrawable(context, R.drawable.ic_select_tickmark)
    }

    companion object {
        const val DEFAULT_POINT_COUNT = 5
        const val TEXT_SIZE_ONES = 2
        const val TEXT_SIZE_HUNDREDS = 4
    }
}