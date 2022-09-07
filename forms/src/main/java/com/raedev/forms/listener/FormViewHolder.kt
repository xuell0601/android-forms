package com.raedev.forms.listener

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference
import java.util.*

/**
 * FromViewHolder
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class FormViewHolder(inflater: LayoutInflater, parent: ViewGroup, layoutId: Int) :
    RecyclerView.ViewHolder(inflater.inflate(layoutId, parent, false)) {

    private val viewRef = WeakHashMap<Int, WeakReference<View>>()

    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(id: Int): T {
        var reference = viewRef[id]
        if (reference?.get() == null) {
            val view = itemView.findViewById<T>(id)
            reference = WeakReference(view)
            viewRef[id] = reference
        }
        return reference.get() as T
    }

    fun setText(id: Int, text: String?) {
        getView<TextView>(id).text = text
    }

    fun setHint(id: Int, hintText: String?) {
        getView<TextView>(id).hint = hintText
    }


    fun setVisibility(id: Int, show: Boolean) {
        getView<TextView>(id).visibility = if (show) View.VISIBLE else View.GONE
    }

    fun clear() {
        // 清空View缓存
        this.viewRef.forEach { it.value?.clear() }
        this.viewRef.clear()
    }
}