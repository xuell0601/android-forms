package com.raedev.forms.items

import android.text.InputFilter
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import com.raedev.forms.FormInputType
import com.raedev.forms.FormType
import com.raedev.forms.R
import com.raedev.forms.internal.filter.RoundInputFilter
import com.raedev.forms.listener.FormTextChangedListener
import com.raedev.forms.listener.FormViewHolder
import com.raedev.forms.validator.PhoneFormValidator
import com.raedev.forms.view.FormEditText

/**
 * 文本编辑框
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class EditTextFormItem(
    inputType: FormInputType,
    label: String,
    name: String,
    value: String?,
    required: Boolean = false
) : FormItem(label, name, value), FormTextChangedListener {


    override val layoutId: Int = R.layout.form_item_edit_text
    override val formType: Int = FormType.EditText.ordinal

    /** 内部固定的过滤 */
    private var internalFilters: MutableList<InputFilter> = arrayListOf()
    private var inputFilters: MutableList<InputFilter> = arrayListOf()
    private var inputType = EditorInfo.TYPE_CLASS_TEXT

    init {
        this.required = required
        setFormInputType(inputType)
    }

    /** 输入的最大长度 */
    var maxLength: Int = -1
        set(value) {
            field = value
            internalFilters
                .find { it is InputFilter.LengthFilter }
                ?.let { internalFilters.remove(it) }
            if (value > 0) {
                internalFilters.add(InputFilter.LengthFilter(value))
            }
        }

    private val filters: Array<InputFilter>
        get() = (internalFilters + inputFilters).toTypedArray()

    private fun addInputFilter(inputFilter: InputFilter) {
        if (inputFilters.contains(inputFilter)) return
        inputFilters.add(inputFilter)
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
        editText.filters = this.filters
        editText.setSelection(this.value?.length ?: 0)
        editText.visibility = if (viewonly) View.GONE else View.VISIBLE
        if (this.inputType or EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE == this.inputType) {
            // 多行
            editText.minLines = 4
            editText.gravity = Gravity.TOP or Gravity.START
        } else {
            editText.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            editText.minLines = 1
        }
    }

    override fun onBindViewEvent(holder: FormViewHolder) {
        super.onBindViewEvent(holder)
        val editText = holder.getView<FormEditText>(R.id.et_value)
        editText.textChangedListener = this
    }

    override fun onUnBindViewEvent(holder: FormViewHolder) {
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
//       debug("onTextChanged: $label = $value")
        this.onValueChanged(value)
    }

    override fun onFormItemRemoved() {
        super.onFormItemRemoved()
        inputFilters.clear()
    }

    // region 支持的输入类型

    private fun setFormInputType(inputType: FormInputType) {
        when (inputType) {
            FormInputType.Text -> showTextInput()
            FormInputType.MultiText -> showMultiTextInput()
            FormInputType.Number -> showNumberInput()
            FormInputType.Decimal -> showDecimalInput()
            FormInputType.Phone -> showPhoneInput()
        }
    }

    private fun showMultiTextInput() {
        inputType = EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
    }

    private fun showDecimalInput() {
        inputType = EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
        addInputFilter(RoundInputFilter())
    }


    private fun showNumberInput() {
        inputType = EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_FLAG_SIGNED
    }

    private fun showPhoneInput() {
        // 11位的手机号码
        maxLength = 11
        inputType = EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_FLAG_SIGNED
        formValidator = PhoneFormValidator()
    }

    private fun showTextInput() {
        inputType = EditorInfo.TYPE_CLASS_TEXT
    }


    // endregion

}