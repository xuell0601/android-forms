package com.raedev.forms.listener

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.raedev.forms.R
import java.lang.ref.WeakReference
import java.util.*

/**
 * 表单填充FromViewHolder，封装了大部分公共的表单视图操作
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
open class FormViewHolder(inflater: LayoutInflater, parent: ViewGroup, layoutId: Int) :
    RecyclerView.ViewHolder(inflater.inflate(layoutId, parent, false)) {

    /** View缓存 */
    private val viewRef = WeakHashMap<Int, WeakReference<View>>()

    /** 标题布局最大宽度 */
    internal var maxTitleLayoutWidth = -1

    /** 标题布局对齐方式 */
    internal var titleLayoutGravity = -1

    /** 表单标题 */
    private val titleView: TextView?
        get() = getViewOrNull(R.id.tv_title)

    /** 必填View */
    private val requiredView: View?
        get() = getViewOrNull(R.id.tv_required)

    /** 只读表单值 */
    private val valueTextView: TextView?
        get() = getViewOrNull(R.id.tv_value)


    /** 表单值EditText */
    internal val valueEditText: EditText?
        get() = getViewOrNull(R.id.et_value)

    /**
     * 找VIEW，没有抛异常
     */
    internal fun <T : View> getView(id: Int): T {
        var reference = viewRef[id]
        if (reference?.get() == null) {
            val view = itemView.findViewById<T>(id)
            reference = WeakReference(view)
            viewRef[id] = reference
        }
        return reference.get() as T
    }

    /**
     * 找VIEW，返回可空
     */
    internal fun <T : View> getViewOrNull(id: Int): T? {
        var reference = viewRef[id]
        if (reference?.get() == null) {
            val view = itemView.findViewById<T>(id) ?: return null
            reference = WeakReference(view)
            viewRef[id] = reference
        }
        return reference.get() as T?
    }

    /**
     * 设置表单标题
     */
    internal fun setTitle(text: String) {
        titleView?.text = text
    }

    /**
     * 设置必填状态
     */
    internal fun setRequired(required: Boolean) {
        requiredView?.visibility = if (required) View.VISIBLE else View.GONE
    }

    /**
     * 设置文本
     */
    internal fun setText(id: Int, text: String?) {
        getView<TextView>(id).text = text
    }

    /**
     * 设置表单值，一般是EditText
     */
    internal fun setValue(value: String?) {
        valueTextView?.text = value
        valueEditText?.setText(value)
    }

    /**
     * 设置提示信息，一般是TextView
     */
    internal fun setHint(id: Int, hintText: String?) {
        getView<TextView>(id).hint = hintText
    }

    /**
     * 设置可见状态
     */
    internal fun setVisibility(id: Int, show: Boolean) {
        getView<TextView>(id).visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * 清空缓存
     */
    internal fun clear() {
        // 清空View缓存
        this.viewRef.forEach { it.value?.clear() }
        this.viewRef.clear()
    }

    /**
     * 查找标题布局
     */
    internal fun findTitleLayout(): ViewGroup {
        return this.getView(R.id.title_layout)
    }

    /**
     * 更新标题布局
     */
    internal fun updateTitleLayout() {
        if (maxTitleLayoutWidth == -1) return
        this.getView<ViewGroup>(R.id.title_layout).apply {
            if (this is LinearLayout) this.gravity = titleLayoutGravity
            if (this.layoutParams.width == maxTitleLayoutWidth) return@apply
            this.updateLayoutParams { this.width = maxTitleLayoutWidth }
        }

    }

    /**
     * 设置只读状态
     */
    internal fun setViewOnly(viewonly: Boolean) {
        requiredView?.visibility = if (viewonly) View.GONE else View.VISIBLE
        valueTextView?.visibility = if (viewonly) View.VISIBLE else View.GONE
        valueEditText?.visibility = if (viewonly) View.GONE else View.VISIBLE
    }
}