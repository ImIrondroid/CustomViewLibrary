package com.iron.library

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * @author 최철훈
 * @created 2022-06-23
 * @desc
 */
class ColorPickerBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var onSelectListener: (Int) -> Unit

    private var color = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_bottom_sheet_color_picker, container, false)

        initializeView(view)

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)
        bottomSheetDialog.setOnShowListener {
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

            if (bottomSheet != null) {
                val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
                behavior.isDraggable = false
            }
        }
        return bottomSheetDialog
    }

    private fun initializeView(view: View) {
        view.findViewById<ColorPicker>(R.id.colorPicker).setOnColorChangeListener {
            color = it
        }

        view.findViewById<TextView>(R.id.selectTextView).setOnClickListener {
            onSelectListener.invoke(color)
            dismiss()
        }
    }

    fun setOnSelectListener(onSelectListener: (Int) -> Unit) {
        this.onSelectListener = onSelectListener
    }

    companion object {
        const val TAG = "ScratchBottomSheetDialogFragment"
    }
}