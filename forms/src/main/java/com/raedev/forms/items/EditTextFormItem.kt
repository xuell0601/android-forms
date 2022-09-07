package com.raedev.forms.items

import android.util.Log
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

    override fun onBindViewHolder(holder: FormViewHolder) {
        holder.setVisibility(R.id.tv_required, this.required)
        holder.setText(R.id.tv_title, this.label)
        val editText = holder.getView<FormEditText>(R.id.et_value)
        onBindEditText(editText)
    }

    protected open fun onBindEditText(editText: FormEditText) {
        editText.setText(this.value)
        editText.hint = this.hint
        editText.textChangedListener = this
    }

    override fun onViewDetachedFromWindow(holder: FormViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val editText = holder.getView<FormEditText>(R.id.et_value)
        editText.textChangedListener = null
    }


    override fun onTextChanged(view: FormEditText, value: String) {
        // 文本发生改变
        Log.d(TAG, "onTextChanged: $label = $value")
        this.onValueChanged(value)
    }

}