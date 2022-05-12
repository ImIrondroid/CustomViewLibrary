package com.iron.library

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ProgressBar
import android.graphics.RectF
import android.util.Log
import com.iron.util.ContextUtil
import com.iron.util.ifLet

/**
 * @author 최철훈
 * @created 2022-03-08
 * @desc ArcProgressBar
 */
class ArcProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ProgressBar(context, attrs, defStyleAttr) {

    private var mWidth: Int = ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_2000)
    private var mHeight: Int = ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_2500)
    private var mStartAngle: Float = 0f
    private var mSweepAngle: Float = 360f

    private val DEFAULT_LINEHEIGHT = ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_100)
    private val DEFAULT_RADIUS = ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_450)
    private val DEFAULT_mUnmProgressColor = -0x151516
    private val DEFAULT_mProgressColor = Color.YELLOW

    private var mbackgroundProgressColor: Int =DEFAULT_mUnmProgressColor
    private var mProgressColor: Int = DEFAULT_mProgressColor
    private var mBoardWidth: Int = DEFAULT_LINEHEIGHT
    private var mArcPadding = ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_100)

    private var mArcPaint: Paint? = null
    private var mArcRectF: RectF? = null
    private var isCapRound = false

    init {
        setAttributeSet(attrs)

        setArcPaint()
    }

    private fun setAttributeSet(attrs: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ArcProgressBar)

        mStartAngle = attributes.getInt(R.styleable.ArcProgressBar_startAngle, 0).toFloat()
        mSweepAngle = attributes.getInt(R.styleable.ArcProgressBar_sweepAngle, 360).toFloat()

        mBoardWidth = attributes.getDimensionPixelOffset(R.styleable.ArcProgressBar_borderWidth, DEFAULT_LINEHEIGHT)
        mProgressColor = attributes.getColor(R.styleable.ArcProgressBar_progressColor, DEFAULT_mProgressColor)
        mbackgroundProgressColor = attributes.getColor(R.styleable.ArcProgressBar_backgroundProgressColor, DEFAULT_mUnmProgressColor)

        isCapRound = attributes.getBoolean(R.styleable.ArcProgressBar_capRound, false)
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        mWidth = when(widthMode) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(widthMeasureSpec)
            MeasureSpec.AT_MOST -> MeasureSpec.getSize(widthMeasureSpec)
            else -> {
                //MeasureSpec.UNSPECIFIED: 동적으로 추가된 경우 width 또는 constraint 없을시 디폴트 사이즈로 대체
                ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_2000)
            }
        }

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        mHeight = when(heightMode) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(heightMeasureSpec)
            MeasureSpec.AT_MOST -> mWidth
            else -> {
                //MeasureSpec.UNSPECIFIED: 동적으로 추가된 경우 width 또는 constraint 없을시 디폴트 사이즈로 대체
                ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_2500)
            }
        }

        setMeasuredDimension(mWidth, mHeight)
    }

    @Synchronized
    override fun onDraw(canvas: Canvas) {
        val progressInterval = progress / max.toFloat()
        val sweepAngle = mSweepAngle * progressInterval

        ifLet(mArcPaint, mArcRectF) { (arcPaint, arcRectF) ->
            (arcPaint as Paint).color = mbackgroundProgressColor
            canvas.drawArc(arcRectF as RectF, mStartAngle, mSweepAngle , false, arcPaint as Paint)

            arcPaint.color = mProgressColor
            canvas.drawArc(arcRectF, mStartAngle, sweepAngle, false, arcPaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mArcRectF = RectF(
            mBoardWidth/2.toFloat(),
            mBoardWidth/2.toFloat(),
            (mWidth - mBoardWidth/2).toFloat(),
            (mHeight - mBoardWidth/2).toFloat()
        )
    }

    private fun setArcPaint() {
        mArcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mbackgroundProgressColor
            style = Paint.Style.STROKE
            strokeWidth = mBoardWidth.toFloat()
            if(isCapRound) strokeCap = Paint.Cap.ROUND
        }
    }
}