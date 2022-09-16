package com.raedev.forms.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.raedev.forms.listener.FormViewHolder
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * 表单项Item装饰
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("MemberVisibilityCanBePrivate")
class FormItemDecoration(
    context: Context,
    color: Int = -1
) : ItemDecoration() {

    companion object {
        val ATTRS: IntArray = intArrayOf(android.R.attr.listDivider)
    }

    private val drawable: Drawable

    private val bounds = Rect()

    /** 底部填充距离 */
    var paddingTop: Int = -1

    /** 底部填充距离 */
    var paddingBottom: Int = -1

    var height: Int = 1

    private val dividerHeight: Int
        get() = max(height, drawable.intrinsicHeight)


    init {
        if (color == -1) {
            val a = context.obtainStyledAttributes(ATTRS)
            drawable = a.getDrawable(0) ?: ColorDrawable(Color.TRANSPARENT)
            a.recycle()
        } else {
            drawable = ColorDrawable(color)
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null) return
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left, parent.paddingTop, right, parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, bounds)
            val holder = parent.getChildViewHolder(child) as FormViewHolder
            var itemDividerHeight = holder.getDividerHeight()
            if (itemDividerHeight == -1) itemDividerHeight = dividerHeight
            var bottom: Int = bounds.bottom + child.translationY.roundToInt()
            if (i == childCount - 1 && paddingBottom > 0) bottom -= paddingBottom
            val top: Int = bottom - itemDividerHeight
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        var top = 0
        var bottom = dividerHeight
        if (position == 0 && paddingTop != -1) {
            top = paddingTop
        }
        if (position == state.itemCount - 1 && paddingBottom != -1) {
            bottom += paddingBottom
        }
        outRect.set(0, top, 0, bottom)
    }

}