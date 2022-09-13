package com.raedev.forms.items

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentManager
import com.raedev.forms.FormGroup
import com.raedev.forms.filter.FormValueFilter
import com.raedev.forms.listener.FormChangedListener
import com.raedev.forms.listener.FormViewHolder
import com.raedev.forms.render.IFormDataBinding
import com.raedev.forms.validator.FormValidator
import com.raedev.forms.validator.RequiredFormValidator
import java.util.*

/**
 * 表单项
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class FormItem(
    /** 表单标签，一般是展示的标题 */
    val label: String,
    /** 表单名，一般是表单字段名 */
    val name: String,
    /** 表单值，以String类型进行保存，设置和获取的时候需手动转换 */
    var value: String?
) {

    protected companion object {
        const val TAG = "RAE.FormItem"
    }

    // region 字段定义

    abstract val layoutId: Int

    /** 表单类型，实际为RecycleView.Adapter中的ViewType */
    abstract val formType: Int

    /** 表单校验器 */
    var formValidator: FormValidator? = null

    /** 表单监听器 */
    var listener: FormChangedListener? = null

    /** 表单过滤器 */
    var filter: FormValueFilter? = null

    /** 表单提示信息，等同于EditText中hint */
    var hint: String? = null
        get() {
            if (field == null) {
                return "请输入$label"
            }
            return field
        }

    /** 表单在FromGroup中的索引 */
    var position: Int = -1
        internal set

    /** 是否可用状态，false时不可以编辑 */
    var enabled: Boolean = true

    /** 是否为只查看状态 */
    var viewonly: Boolean = false

    /** 是否为必填项 */
    var required: Boolean = false
        set(value) {
            field = value
            when (value) {
                true -> {
                    formValidator = formValidator ?: RequiredFormValidator()
                }
                false -> {
                    if (formValidator is RequiredFormValidator) formValidator = null
                }
            }

        }

    /**
     * 自动绑定对象
     */
    private var dataBinding: IFormDataBinding? = null

    /** 表单组 */
    private var formGroup: FormGroup? = null

    /** FragmentManager */
    private var fragmentManager: FragmentManager? = null

    /** 父表单 */
    internal var parent: FormItem? = null

    // endregion

    // region 子表单操作
//
//    /**
//     * 添加子表单
//     */
//    fun addChildren(item: FormItem): Boolean {
//        if (children.contains(item)) return false
//        return children.add(item)
//    }
//
//    /**
//     * 移除子表单
//     */
//    fun removeChildren(item: FormItem) {
//        children.remove(item)
//    }
//
//    /**
//     * 移除所有子表单
//     */
//    fun removeAll() {
//        children.clear()
//    }

    // endregion

    /** 值发生改变的时候由子类负责调用，进行表单值的流程，最后赋值给当前表单 */
    protected fun onValueChanged(value: String?) {
        // 进行拦截过滤
        val filterValue = if (filter != null) filter!!.filter(this, value) else value
        // 通知值发生改变
        listener?.onValueChanged(this, value, this.value)
        // 最后设置值
        this.value = filterValue
        // 更新绑定的值
        dataBinding?.updateValue(this)
        if (filterValue != value) selfRefresh()
    }

    // region 生命周期

    protected fun selfRefresh() = formGroup?.refreshItem(this)

    /**
     * 创建ViewHolder
     */
    open fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): FormViewHolder {
        return FormViewHolder(inflater, parent, layoutId)
    }

    /**
     * 绑定ViewHolder
     */
    internal fun onBindViewHolderOnAdapter(holder: FormViewHolder) {
        // 取消绑定事件
        onUnBindViewEvent(holder)
        // 默认设置
        holder.maxTitleLayoutWidth = formGroup?.maxTitleWidth ?: -1
        holder.setRequired(this.required)
        holder.setTitle(this.label)
        holder.setValue(this.value)
        holder.setViewOnly(this.viewonly)
        // 绑定View
        onBindViewHolder(holder)
        // 最后绑定事件
        onBindViewEvent(holder)
    }

    protected abstract fun onBindViewHolder(holder: FormViewHolder)

    /**
     * 回收View的时候触发
     */
    open fun onRecycled(holder: FormViewHolder) {
        onUnBindViewEvent(holder)
    }

    /**
     * View展示在窗口的时候触发
     */
    open fun onViewAttachedToWindow(holder: FormViewHolder) {

    }

    /**
     * View从窗口移除的时候触发
     */
    open fun onViewDetachedFromWindow(holder: FormViewHolder) {
        onUnBindViewEvent(holder)
    }

    protected open fun onBindViewEvent(holder: FormViewHolder) = Unit

    protected open fun onUnBindViewEvent(holder: FormViewHolder) = Unit

    /**
     * 添加到FormGroup中时触发
     */
    open fun onFormItemAdded(
        indexOf: Int,
        formGroup: FormGroup,
        fragmentManager: FragmentManager?,
        dataBinding: IFormDataBinding?
    ) {
        this.formGroup = formGroup
        this.position = indexOf
        this.dataBinding = dataBinding
        this.fragmentManager = fragmentManager
    }

    /**
     * 从FormGroup中移除的时候触发
     */
    open fun onFormItemRemoved() {
        this.formGroup = null
        this.dataBinding = null
    }

    /**
     * 测量布局，返回左边标题宽度和右边内容宽度
     * @return array(left, right)
     */
    open fun getTitleLayoutWidth(holder: FormViewHolder): Int {
        return 0
    }

    /**
     * 开始布局
     */
    fun layout(holder: FormViewHolder) {
        if (holder.maxTitleLayoutWidth <= 0) return
        Log.d(TAG, "layout: $label; ${holder.maxTitleLayoutWidth}")
        onLayoutChildren(holder)
    }

    protected open fun onLayoutChildren(holder: FormViewHolder) = Unit

    /**
     * 更新标题布局
     */
    protected fun updateTitleLayoutWidth(layout: View, width: Int) {
        if (layout.layoutParams.width == width) return
        layout.forceLayout()
        layout.updateLayoutParams { this.width = width }
        Log.i(TAG, "更新标题布局: $label ")
    }


    // endregion

    // region 重写方法

    /**
     * 相等判断：表单名一样看做是同一个FormItem
     */
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null || other.javaClass != this.javaClass) return false
        other as FormItem
        return this.name == other.name
    }

    override fun hashCode(): Int {
        return Objects.hashCode(name)
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName}: label=$label, name=$name, value=$value, enable=$enabled, required=$required, viewonly=$viewonly"
    }


    // endregion

}