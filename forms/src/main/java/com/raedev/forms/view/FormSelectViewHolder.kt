package com.raedev.forms.view

import android.view.View
import android.widget.ImageView
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
    private val arrowView: ImageView = itemView.findViewById(R.id.img_arrow)
    fun setLabel(text: String) {
        textView.text = text
    }

    fun showArrow(show: Boolean) {
        arrowView.visibility = if (show) View.VISIBLE else View.GONE
    }
}