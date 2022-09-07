package com.raedev.forms.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.raedev.forms.filter.FormValueFilter
import com.raedev.forms.internal.binder.FormEntityReference
import com.raedev.forms.listener.FormChangedListener
import com.raedev.forms.listener.FormViewHolder
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
        const val TAG = "FormItem"
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
    val hint: String? = null
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
    internal var formEntity: FormEntityReference? = null

    /** 子表单项 */
    private val children by lazy { mutableListOf<FormItem>() }


    /** 子表单数量 */
    val childrenCount: Int
        get() = children.count()

    /** 当前表单在子表单项的位置 */
    var childrenPosition: Int = -1


    // endregion

    // region 子表单操作

    /**
     * 添加子表单
     */
    fun addChildren(item: FormItem): Boolean {
        if (children.contains(item)) return false
        return children.add(item)
    }

    /**
     * 移除子表单
     */
    fun removeChildren(item: FormItem) {
        children.remove(item)
    }

    /**
     * 移除所有子表单
     */
    fun removeAll() {
        children.clear()
    }

    // endregion

    /** 值发生改变的时候由子类负责调用，进行表单值的流程，最后赋值给当前表单 */
    protected fun onValueChanged(value: String?) {
        // 进行拦截过滤
        val filterValue = filter?.filter(this, value) ?: value
        // 通知值发生改变
        listener?.onValueChanged(this, value, this.value)
        // 最后设置值
        this.value = filterValue
        // 更新绑定的值
        formEntity?.setValue(name, value)
    }

    // region 生命周期

    /**
     * 创建ViewHolder
     */
    open fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): FormViewHolder {
        return FormViewHolder(inflater, parent, this.layoutId)
    }

    /**
     * 绑定ViewHolder
     */
    abstract fun onBindViewHolder(holder: FormViewHolder)

    /**
     * 回收View的时候触发
     */
    open fun onRecycled(holder: FormViewHolder) {
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