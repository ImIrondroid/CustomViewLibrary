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
    private var mHeight: Int = ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_275)
    private var mStartAngle: Float = 0f
    private var mSweepAngle: Float = 360f

    private val DEFAULT_LINEHEIGHT = ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_100)
    private val DEFAULT_RADIUS = ContextUtil.dpToPx(context, com.iron.util.R.dimen.normal_450)
    private val DEFAULT_mUnmProgressColor = -0x151516
    private val DEFAULT_mProgressColor = Color.YELLOW
    private val DEFAULT_OFFSETDEGREE = 60

    private var mRadius: Float = DEFAULT_RADIUS.toFloat()
    private var mArcBackgroundColor: Int = DEFAULT_mUnmProgressColor
    private var mUnmProgressColor: Int =DEFAULT_mUnmProgressColor
    private var mProgressColor: Int = DEFAULT_mProgressColor
    private var mBoardWidth: Int = DEFAULT_LINEHEIGHT
    private var mDegree = DEFAULT_OFFSETDEGREE
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
        mUnmProgressColor = attributes.getColor(R.styleable.ArcProgressBar_unprogresColor, DEFAULT_mUnmProgressColor)

        mRadius = attributes.getDimensionPixelOffset(R.styleable.ArcProgressBar_radius, DEFAULT_RADIUS).toFloat()
        mArcBackgroundColor = attributes.getColor(R.styleable.ArcProgressBar_arcbgColor, DEFAULT_mUnmProgressColor)

        mDegree = attributes.getInt(R.styleable.ArcProgressBar_degree, DEFAULT_OFFSETDEGREE)
        isCapRound = attributes.getBoolean(R.styleable.ArcProgressBar_capRound, false)
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var mWidthMeasureSpec = widthMeasureSpec
        var mHeightMeasureSpec = heightMeasureSpec
        val widthMode = MeasureSpec.getMode(mWidthMeasureSpec)
        val heightMode = MeasureSpec.getMode(mHeightMeasureSpec)

        if (widthMode != MeasureSpec.EXACTLY) {
            val widthSize = (mRadius * 2 + mBoardWidth * 2).toInt()
            mWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY)
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            val heightSize = (mRadius * 2 + mBoardWidth * 2).toInt()
            mHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY)
        }

        super.onMeasure(mWidthMeasureSpec, mHeightMeasureSpec)
    }

    @Synchronized
    override fun onDraw(canvas: Canvas) {
        val progressInterval = progress / max.toFloat()
        val sweepAngle = mSweepAngle * progressInterval

        ifLet(mArcPaint, mArcRectF) { (arcPaint, arcRectF) ->
            (arcPaint as Paint).color = mUnmProgressColor
            canvas.drawArc(arcRectF as RectF, mStartAngle, mSweepAngle , false, arcPaint)

            arcPaint.color = mProgressColor
            canvas.drawArc(arcRectF, mStartAngle, sweepAngle, false, arcPaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mArcRectF = RectF(
            mBoardWidth/2.toFloat(),
            mBoardWidth/2.toFloat(),
            (width - mBoardWidth/2).toFloat(),
            (height - mBoardWidth/2).toFloat()
        )
    }

    private fun setArcPaint() {
        mArcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mArcBackgroundColor
            style = Paint.Style.STROKE
            strokeWidth = mBoardWidth.toFloat()
            if(isCapRound) strokeCap = Paint.Cap.ROUND
        }
    }
}