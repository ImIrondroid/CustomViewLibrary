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
    private var index: Int = 0
    private var currentIndex: Int = 0

    override fun showPrevious() {
        super.showPrevious()

        onViewChangeListener?.invoke(currentView, index)
        currentIndex = index

        index--
        if (index < 0) index = 0
    }

    override fun showNext() {
        super.showNext()

        onViewChangeListener?.invoke(currentView, index)
        currentIndex = index

        index++
        index = if (count == null) {
            index % childCount
        } else {
            index % count!!
        }
    }

    fun setCount(count: Int) {
        this.count = count
    }

    fun getCurrentIndex() = currentIndex

    fun setOnViewChangeListener(onViewChangeListener: (View, Int) -> Unit) {
        this.onViewChangeListener = onViewChangeListener
    }
}