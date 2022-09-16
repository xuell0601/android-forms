package com.raedev.forms.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raedev.forms.R

/**
 * 表单选择项
 * @author RAE
 * @date 2022/09/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
internal class FormSelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.tv_label)
    private val nextArrowView: View = itemView.findViewById(R.id.img_next_arrow)
    private val backArrowView: View = itemView.findViewById(R.id.img_back_arrow)
    internal val checkView: View = itemView.findViewById(R.id.tv_check)

    fun setLabel(text: String) {
        textView.text = text
    }

    /**
     * 显示下一级箭头
     */
    fun showArrow(show: Boolean) {
        nextArrowView.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * 显示返回箭头
     */
    fun showBackArrow(show: Boolean) {
        backArrowView.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * 显示选择按钮
     */
    fun showCheckView(show: Boolean) {
        checkView.visibility = if (show) View.VISIBLE else View.GONE
    }
}