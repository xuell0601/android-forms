package com.raedev.forms.items

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.raedev.forms.BuildConfig
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
    var value: String?,

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
    private var listener: FormChangedListener? = null

    /** 表单过滤器 */
    var filter: FormValueFilter? = null

    /** 表单提示信息，等同于EditText中hint */
    open var hint: String? = null
        get() = if (field == null) "请输入$label" else field

    /** 表单在FromGroup中的索引 */
    val position: Int
        get() = formGroup?.indexOf(this) ?: -1

    /** 是否可用状态，false时不可以编辑 */
    var enabled: Boolean = true

    /** 是否为只查看状态 */
    var viewonly: Boolean = false

    /** 是否为必填项 */
    var required: Boolean = false
        set(value) {
            field = value
            // 没有设置校验器的时候自动添加
            if (value && formValidator == null)
                formValidator = RequiredFormValidator()
            // 已经设置校验器的时候取消置空
            if (!value && formValidator is RequiredFormValidator)
                formValidator = null
        }

    /** 表单组 */
    internal var formGroup: FormGroup? = null

    /**
     * 自动绑定对象
     */
    protected var dataBinding: IFormDataBinding? = null

    /** FragmentManager */
    protected var fragmentManager: FragmentManager? = null


    // endregion

    // region 子表单操作，内部转发给FormGroup实现。

    /** 父表单 */
    internal var parent: FormItem? = null

    /** 层级关系 */
    private val level: Int
        get() = findLevel(parent)

    /**
     * 递归找父表单层级
     */
    private fun findLevel(parent: FormItem?, level: Int = 0): Int {
        if (parent == null) return level
        return findLevel(parent.parent, level + 1)
    }

    /**
     * 移除所有子表单
     */
    fun removeAllChildren() = formGroup?.removeAllChildren(this)

    /**
     * 添加子表单
     */
    fun addChildren(item: FormItem) {
        if (formGroup == null) throw NullPointerException("请先将当前表单[$label]添加到表单组中后再添加子表单")
        formGroup!!.addChildren(this, item)
    }

    /**
     * 移除子表单
     */
    fun removeChildren(name: String) = formGroup?.removeItem(name)


    // endregion


    /** 值发生改变的时候由子类负责调用，进行表单值的流程，最后赋值给当前表单 */
    protected fun onValueChanged(value: String?) {
        // 进行拦截过滤
        val filterValue = if (filter != null) filter!!.filter(this, value) else value
        // 值由更新的时候刷新自己
        if (filterValue != value || this.value != filterValue) selfRefresh()
        // 设置值
        this.value = filterValue
        // 通知值发生改变
        listener?.onValueChanged(this, value, this.value)
        // 更新绑定的值
        dataBinding?.updateValue(this)
    }

    /**
     * 刷新当前表单
     */
    private fun selfRefresh() = formGroup?.refreshItem(this)

    /** 输出调试信息 */
    protected fun debug(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }

    /**
     * 查找焦点View
     */
    fun focusSearch(holder: FormViewHolder): View? {
        return null
    }


    // region 生命周期


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
        // 先取消绑定事件
        onUnBindViewEvent(holder)
        // 默认公共的视图绑定
        holder.maxTitleLayoutWidth = formGroup?.maxTitleWidth ?: -1
        holder.setTitle(this.label)
        holder.setValue(this.value)
        holder.setViewOnly(this.viewonly)
        holder.setRequired(this.required)
        // 绑定View
        onBindViewHolder(holder)
        // 最后绑定事件
        if (!viewonly) onBindViewEvent(holder)
    }

    /**
     * 由子类实现视图绑定方法
     */
    protected abstract fun onBindViewHolder(holder: FormViewHolder)

    /**
     * 回收View的时候触发
     */
    open fun onRecycled(holder: FormViewHolder) {
        onUnBindViewEvent(holder)
    }

    /**
     * 绑定View事件重写该方法，自动解决事件冲突处理。
     */
    protected open fun onBindViewEvent(holder: FormViewHolder) = Unit

    /**
     * 取消View事件绑定
     */
    protected open fun onUnBindViewEvent(holder: FormViewHolder) = Unit

    /**
     * View展示在窗口的时候触发
     */
    open fun onViewAttachedToWindow(holder: FormViewHolder) {
        if (!viewonly) onBindViewEvent(holder)
    }

    /**
     * View从窗口移除的时候触发
     */
    open fun onViewDetachedFromWindow(holder: FormViewHolder) {
        onUnBindViewEvent(holder)
    }


    /**
     * 设置表单值改变监听
     */
    fun setFormChangedListener(function: (FormItem, String?, String?) -> Unit) {
        this.listener = object : FormChangedListener {
            override fun onValueChanged(item: FormItem, value: String?, old: String?) {
                function(item, value, old)
            }
        }
        // 默认回调一次，使得表单在逻辑关联的场景下自动触发
        this.listener!!.onValueChanged(this, this.value, null)
    }

    /**
     * 添加到FormGroup中时触发
     */
    open fun onFormItemAdded(indexOf: Int, formGroup: FormGroup) {
        this.formGroup = formGroup
    }

    /**
     * 渲染的时候触发
     */
    open fun onFormRender(fragmentManager: FragmentManager?, dataBinding: IFormDataBinding?) {
        this.dataBinding = dataBinding
        this.fragmentManager = fragmentManager
    }

    /**
     * 这是真正的移除，从FormGroup中移除的时候触发，释放所有关联的资源
     */
    open fun onFormItemRemoved() {
        this.formGroup = null
        this.dataBinding = null
        this.fragmentManager = null
        this.dataBinding = null
        this.parent = null
        this.filter = null
        this.value = null
    }

    /**
     * 测量布局，返回左边标题宽度和右边内容宽度
     * @return array(left, right)
     */
    open fun getTitleLayoutWidth(holder: FormViewHolder): Int = 0

    /**
     * 开始布局
     */
    fun layout(holder: FormViewHolder) {
        if (holder.maxTitleLayoutWidth <= 0) return
        onLayoutChildren(holder)
    }

    /**
     * 当FormLayoutManager请求重新布局的时候触发由表单自己选择是否需要重新布局。
     */
    protected open fun onLayoutChildren(holder: FormViewHolder) = Unit


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
        return "${this.javaClass.simpleName}: label=$label, name=$name, value=$value, enable=$enabled, required=$required, viewonly=$viewonly, level=$level"
    }

    // endregion

}