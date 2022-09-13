package com.raedev.forms.items

import android.text.InputFilter
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import com.raedev.forms.FormItemType
import com.raedev.forms.R
import com.raedev.forms.listener.FormTextChangedListener
import com.raedev.forms.listener.FormViewHolder
import com.raedev.forms.view.FormEditText

/**
 * 文本编辑框
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class EditTextFormItem(label: String, name: String, value: String?) :
    FormItem(label, name, value),
    FormTextChangedListener {

    override val layoutId: Int = R.layout.form_item_edit_text
    override val formType: Int = FormItemType.EditText.ordinal
    protected var inputFilter: MutableList<InputFilter> = arrayListOf()
    protected var inputType = EditorInfo.TYPE_CLASS_TEXT

    /** 输入的最大长度 */
    var maxLength: Int = -1
        set(value) {
            field = value
            // 移除已经有的
            inputFilter.find { it is InputFilter.LengthFilter }?.let {
                inputFilter.remove(it)
            }
            // 添加长度过滤
            inputFilter.add(InputFilter.LengthFilter(value))
        }

    override fun onBindViewHolder(holder: FormViewHolder) {
        // 更新标题布局
        holder.updateTitleLayout()
        onBindEditText(holder.valueEditText as FormEditText)
    }

    /**
     * 绑定文本输入框
     */
    protected open fun onBindEditText(editText: FormEditText) {
        editText.hint = this.hint
        editText.isEnabled = enabled
        editText.inputType = this.inputType
        editText.filters = this.inputFilter.toTypedArray()
        editText.setSelection(this.value?.length ?: 0)
        editText.visibility = if (viewonly) View.GONE else View.VISIBLE
    }

    override fun onBindViewEvent(holder: FormViewHolder) {
        super.onBindViewEvent(holder)
        val editText = holder.getView<FormEditText>(R.id.et_value)
        editText.textChangedListener = this
    }

    override fun onUnBindViewEvent(holder: FormViewHolder) {
        super.onUnBindViewEvent(holder)
        val editText = holder.getView<FormEditText>(R.id.et_value)
        editText.textChangedListener = null
    }

    override fun getTitleLayoutWidth(holder: FormViewHolder): Int {
        return holder.findTitleLayout().measuredWidth
    }

    override fun onLayoutChildren(holder: FormViewHolder) {
        holder.updateTitleLayout()
    }

    override fun onTextChanged(view: FormEditText, value: String) {
        // 文本发生改变
        Log.d(TAG, "onTextChanged: $label = $value")
        this.onValueChanged(value)
    }

}