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
import java.math.BigDecimal

/**
 * @author iron.choi
 * @created 2022-04-06
 * @desc TickMark와 ThumbMark가 적용된 CustomSeekBar 클래스
 *       - width, height, padding의 기본값 설정 및 커스텀 대응
 *       - TickMarkCount 입력값에 맞게 화면 설정 가능
 *       - ProgressBarColor(Select : [app:progressColor="@color/black"] / UnSelect : app:lineColor="@color/black")와 같이 설정 가능
 *       - Max값 사용자 설정에 따라 ThumbMark와 ThumbMarkText의 유동적인 위치 노출
 *       - ThumbMarkTextColor 설정 가능
 *       - HD, FHD, WQHD 디스플레이 해상도 대응
 */
class PointSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatSeekBar(context, attrs, defStyle) {

    private var isTouched = false
    private var tickMarkCount = DEFAULT_POINT_COUNT

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

        tickMarkCount = attributes.getInt(R.styleable.CustomSeekBar_tickMarkCount, DEFAULT_POINT_COUNT)
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
        val rangeInterval: Float = (mWidth - mPaddingStart - mPaddingEnd) / max.toFloat()
        val progressInterval = rangeInterval * progress

        ifLet(mLinePaint, mProgressPaint) { (linePaint, progressPaint) ->
            canvas.apply {
                drawLine(
                    mPaddingStart.toFloat(),
                    progressHeight.toFloat(),
                    (mWidth - mPaddingEnd).toFloat(),
                    progressHeight.toFloat(),
                    linePaint
                )
                drawLine(
                    mPaddingStart.toFloat(),
                    progressHeight.toFloat(),
                    mPaddingStart + progressInterval,
                    progressHeight.toFloat(),
                    progressPaint
                )
            }
        }
    }

    private fun drawTickMark(canvas: Canvas) {
        val pointRangeCount = tickMarkCount - 1
        val tickMarkInterval: Float = (mWidth - mPaddingStart - mPaddingEnd) / pointRangeCount.toFloat()
        val progressHeight = mHeight / 4 * 3
        val selectCount = (progress * (tickMarkCount - 1)) / max

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
            canvas.translate((mWidth - mPaddingEnd).toFloat(), progressHeight.toFloat())
            for (i in 0 until (tickMarkCount - selectCount)) {
                unSelectTickMark.draw(canvas)
                canvas.translate(-tickMarkInterval, 0f)
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

            saveCount = canvas.save()
            canvas.translate(mPaddingStart.toFloat(), progressHeight.toFloat())
            for (i in 0..selectCount) {
                selectTickMark.draw(canvas)
                canvas.translate(tickMarkInterval, 0f)
            }
            canvas.restoreToCount(saveCount)
        }
    }

    private fun drawThumbMark(canvas: Canvas) {
        val rangeInterval: Float = (mWidth - mPaddingStart - mPaddingEnd) / max.toFloat()
        val progressInterval = rangeInterval * progress
        val thumbHeight = mHeight.toFloat() / 14

        ifLet(mThumbMarkFirst, mThumbMarkSecond, mThumbMarkThird, mThumbMarkTextPaint) { (thumbMarkFirst, thumbMarkSecond, thumbMarkThird, textPaint) ->
            if (isTouched) {
                val thumbMarkWidth = (thumbMarkSecond as Bitmap).width
                val markExtraPadding =
                    when (progress) {
                        0 -> ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_25)
                        max -> ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_37_5)
                        else -> 0
                    }

                when (progress) {
                    0 -> canvas.drawBitmap(
                        thumbMarkFirst as Bitmap,
                        (mPaddingStart - markExtraPadding).toFloat(),
                        thumbHeight,
                        null
                    )
                    max -> canvas.drawBitmap(
                        thumbMarkThird as Bitmap,
                        (mWidth + markExtraPadding - mPaddingEnd - thumbMarkWidth).toFloat(),
                        thumbHeight,
                        null
                    )
                    else -> canvas.drawBitmap(
                        thumbMarkSecond,
                        mPaddingStart + progressInterval - (thumbMarkWidth / 2),
                        thumbHeight,
                        null
                    )
                }

                val maxLength = max.toString().length
                val thumbTextNumber =
                    if(progress == 0) 0
                    else (BigDecimal(progress.toDouble()).divide(BigDecimal(max.toDouble()), maxLength, BigDecimal.ROUND_HALF_UP))
                        .toFloat()
                        .times(100)
                        .toInt()
                val thumbText = "$thumbTextNumber%"
                val progressTextHeight = thumbHeight * 5
                val thumbMarkPadding = thumbMarkSecond.width / 2.5f
                val textExtraPadding =
                    when {
                        thumbTextNumber == 0 -> ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_25)
                        thumbTextNumber < 10 -> ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_25)
                        thumbTextNumber == 100 -> ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_12_5)
                        else -> 0
                    }

                when (progress) {
                    0 -> canvas.drawText(
                        thumbText,
                        (mPaddingStart + textExtraPadding).toFloat(),
                        progressTextHeight,
                        textPaint as Paint
                    )
                    max -> canvas.drawText(
                        thumbText,
                        (mWidth + markExtraPadding - mPaddingEnd - thumbMarkWidth + textExtraPadding).toFloat(),
                        progressTextHeight,
                        textPaint as Paint
                    )
                    else -> {
                        canvas.drawText(
                            thumbText,
                            mPaddingStart + textExtraPadding + progressInterval - thumbMarkPadding,
                            progressTextHeight,
                            textPaint as Paint
                        )
                    }
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

    fun setTickMarkCount(tickMarkCount: Int) {
        this.tickMarkCount = tickMarkCount
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
            textSize = ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_75).toFloat()
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
    }
}