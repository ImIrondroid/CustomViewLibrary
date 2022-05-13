package com.iron.library

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ProgressBar
import android.graphics.RectF
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
    private var mBackgroundProgressColor: Int = -0x151516
    private var mProgressColor: Int = Color.YELLOW
    private var mBoardWidth: Int = ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_100)
    private var isClockwise: Boolean = true
    private var mStrokeCap = 0

    private var mArcPaint: Paint? = null
    private var mArcRectF: RectF? = null

    init {
        setAttributeSet(attrs)

        setArcPaint()
    }

    private fun setAttributeSet(attrs: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ArcProgressBar)

        mStartAngle = attributes.getInt(R.styleable.ArcProgressBar_startAngle, 0).toFloat()
        mSweepAngle = attributes.getInt(R.styleable.ArcProgressBar_sweepAngle, 360).toFloat()
        mBackgroundProgressColor = attributes.getColor(R.styleable.ArcProgressBar_backgroundProgressColor, -0x151516)
        mProgressColor = attributes.getColor(R.styleable.ArcProgressBar_progressColor, Color.YELLOW)
        mBoardWidth = attributes.getDimensionPixelOffset(R.styleable.ArcProgressBar_borderWidth, ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_100))
        isClockwise = attributes.getBoolean(R.styleable.ArcProgressBar_direction, true)
        mStrokeCap = attributes.getInt(R.styleable.ArcProgressBar_strokeCap, 0)
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
            when(isClockwise) {
                true -> {
                    (arcPaint as Paint).color = mBackgroundProgressColor
                    canvas.drawArc(arcRectF as RectF, mStartAngle, mSweepAngle , false, arcPaint)

                    arcPaint.color = mProgressColor
                    canvas.drawArc(arcRectF, mStartAngle, sweepAngle, false, arcPaint)
                }

                false -> {
                    (arcPaint as Paint).color = mBackgroundProgressColor
                    canvas.drawArc(arcRectF as RectF, mStartAngle, -mSweepAngle , false, arcPaint)

                    arcPaint.color = mProgressColor
                    canvas.drawArc(arcRectF, mStartAngle, -sweepAngle, false, arcPaint)
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mArcRectF = RectF(
            mBoardWidth / 2 + paddingStart.toFloat(),
            mBoardWidth / 2 + paddingTop.toFloat(),
            (mWidth - mBoardWidth / 2) - paddingEnd.toFloat(),
            (mHeight - mBoardWidth / 2) - paddingBottom.toFloat()
        )
    }

    private fun setArcPaint() {
        mArcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mBackgroundProgressColor
            style = Paint.Style.STROKE
            strokeWidth = mBoardWidth.toFloat()
            strokeCap =
                when(mStrokeCap) {
                    0 -> Paint.Cap.BUTT
                    1 -> Paint.Cap.ROUND
                    else -> Paint.Cap.SQUARE
                }
        }
    }
}