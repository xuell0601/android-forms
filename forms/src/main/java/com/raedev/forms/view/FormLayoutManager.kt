@file:Suppress("MemberVisibilityCanBePrivate")

package com.raedev.forms.view

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raedev.forms.FormGroup
import com.raedev.forms.listener.FormViewHolder
import kotlin.math.min

/**
 * 表单布局管理，主要作用是对表单项进行排列对齐。
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class FormLayoutManager(context: Context, private val formGroup: FormGroup) :
    LinearLayoutManager(context) {

    companion object {
        private const val TAG = "RAE.FormLayoutManager"
    }

    /** 标题最大的比例，默认是父布局的一半 */
    var maxTitleWidthRatio: Float = 0.5f

    private var recyclerView: RecyclerView? = null
    private var maxTitleWidth = -1
    private var isInLayoutChildren = false

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        recyclerView = view
    }

    override fun onDetachedFromWindow(view: RecyclerView?, recycler: RecyclerView.Recycler?) {
        super.onDetachedFromWindow(view, recycler)
        this.recyclerView = null
    }

    private fun getChildViewHolder(child: View): FormViewHolder {
        return recyclerView!!.getChildViewHolder(child) as FormViewHolder
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        // 执行父类的方法，得到布局宽高
        this.isInLayoutChildren = true
        super.onLayoutChildren(recycler, state)
        // 重新刷新布局
        calcTitleMaxWidth()
        layoutChildren()
        this.isInLayoutChildren = false
    }

    override fun measureChildWithMargins(child: View, widthUsed: Int, heightUsed: Int) {
        super.measureChildWithMargins(child, widthUsed, heightUsed)
        if (isInLayoutChildren) return
        val holder = getChildViewHolder(child)
        val position = if (holder.adapterPosition != -1) holder.adapterPosition else return
        calcTitleMaxWidth()
        formGroup[position].layout(holder)
    }

    /**
     * 计算标题最大的宽度
     */
    private fun calcTitleMaxWidth() {
        for (i in 0 until childCount) {
            val view = findViewByPosition(i) ?: continue
            val holder = getChildViewHolder(view)
            val position = if (holder.adapterPosition != -1) holder.adapterPosition else continue
            val formItem = formGroup[position]
            // 测量子表单标题布局
            val childTitleWidth = formItem.getTitleLayoutWidth(holder)
            if (childTitleWidth > maxTitleWidth) {
                // 找最大宽度，并且最大宽度不能是最大父宽度的比例
                maxTitleWidth = min((width * maxTitleWidthRatio).toInt(), childTitleWidth)
            }
        }
        formGroup.maxTitleWidth = maxTitleWidth
    }


    private fun layoutChildren() {
        for (i in 0 until childCount) {
            val view = findViewByPosition(i) ?: continue
            val holder = getChildViewHolder(view)
            val position = if (holder.adapterPosition != -1) holder.adapterPosition else continue
            holder.maxTitleLayoutWidth = maxTitleWidth
            formGroup[position].layout(holder)
        }
    }

    /**
     * 查找表单组的焦点View，解决键盘点击下一项的时候崩溃问题
     */
    override fun onFocusSearchFailed(
        focused: View,
        focusDirection: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): View? {
        val holderView = super.onFocusSearchFailed(focused, focusDirection, recycler, state)
            ?: return null
        val lp = holderView.layoutParams
        if (lp !is RecyclerView.LayoutParams) return holderView
        val position = lp.viewAdapterPosition
        if (position == RecyclerView.NO_POSITION) return holderView
        val item = formGroup[position]
        return item.focusSearch(getChildViewHolder(holderView))
    }


}