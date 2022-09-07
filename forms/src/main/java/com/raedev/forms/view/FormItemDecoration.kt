package com.raedev.forms.view

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

/**
 * 表单项Item装饰
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class FormItemDecoration(context: Context, orientation: Int = VERTICAL) :
    DividerItemDecoration(context, orientation) {


    /** 分割线颜色 */
    @ColorInt
    var color: Int = Color.TRANSPARENT
        set(value) {
            field = value
            setDrawable(ColorDrawable(color))
        }

    /** 底部填充距离 */
    var paddingTop: Int = 0

    /** 底部填充距离 */
    var paddingBottom: Int = 0


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        if (itemPosition == 0) {
            outRect.set(outRect.left, outRect.top + paddingTop, outRect.right, outRect.bottom)
            return
        }
        if (itemPosition == state.itemCount - 1) {
            outRect.set(outRect.left, outRect.top, outRect.right, outRect.bottom + paddingBottom)
        }
    }
}