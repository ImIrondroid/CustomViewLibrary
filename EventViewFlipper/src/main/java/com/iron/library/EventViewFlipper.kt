package com.iron.library

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ViewFlipper

/**
 * @author 최철훈
 * @created 2022-06-17
 * @desc EventViewFlipper
 */
class EventViewFlipper(
    context: Context,
    attrs: AttributeSet? = null
) : ViewFlipper(context, attrs) {

    private var onViewChangeListener: ((View, Int) -> Unit)? = null

    private var count: Int? = null
    private var index: Int = -1

    override fun showPrevious() {
        super.showPrevious()

        index--

        if(count != null) {
            if (index < 0) index = count!! - 1
        } else {
            if (index < 0) index = childCount - 1
        }

        onViewChangeListener?.invoke(currentView, index)
    }

    override fun showNext() {
        super.showNext()

        index++
        index = if (count == null) {
            index % childCount
        } else {
            index % count!!
        }

        onViewChangeListener?.invoke(currentView, index)
    }

    fun setCount(count: Int) {
        this.count = count
    }

    fun setOnViewChangeListener(onViewChangeListener: (View, Int) -> Unit) {
        this.onViewChangeListener = onViewChangeListener
    }
}