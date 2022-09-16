package com.raedev.forms.items

import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import com.raedev.forms.FormType
import com.raedev.forms.R
import com.raedev.forms.listener.FormViewHolder

/**
 * 复选框表单
 * @author RAE
 * @date 2022/09/13
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class CheckBoxFormItem(
    label: String, name: String, value: String?,
    required: Boolean = false
) : FormItem(label, name, value), View.OnClickListener {

    override val layoutId: Int = R.layout.form_item_check_box

    override val formType: Int = FormType.CheckBox.ordinal

    protected open val checkLabels = arrayOf("是", "否")

    override var hint: String? = null

    init {
        this.required = required
    }


    /**
     * 设置选中状态
     */
    open var checked: Boolean
        get() = this.value?.toBooleanStrict() ?: false
        set(value) {
            this.value = value.toString()
        }

    protected open val checkLabel: String
        get() = when (checked) {
            true -> checkLabels[0]
            false -> checkLabels[1]
        }

    protected open fun FormViewHolder.checkBox(): CheckBox {
        return this.getView(R.id.cb_value)
    }


    override fun onBindViewEvent(holder: FormViewHolder) {
        super.onBindViewEvent(holder)
        holder.itemView.tag = holder.checkBox()
        holder.itemView.setOnClickListener(this)
        holder.checkBox().setOnCheckedChangeListener(this::onCheckedChanged)
    }

    override fun onUnBindViewEvent(holder: FormViewHolder) {
        holder.itemView.setOnClickListener(null)
        holder.checkBox().setOnCheckedChangeListener(null)
    }

    /**
     * 设置选中的文本
     * @param checkedText 选中文本
     * @param unCheckedText 不选中文本
     */
    fun setCheckBoxLabel(checkedText: String, unCheckedText: String) {
        checkLabels[0] = checkedText
        checkLabels[1] = unCheckedText
    }

    override fun onBindViewHolder(holder: FormViewHolder) {
        holder.updateTitleLayout()
        holder.checkBox().apply {
            text = checkLabel
            isChecked = checked
            visibility = if (viewonly) View.GONE else View.VISIBLE
        }
        holder.setVisibility(R.id.tv_value, viewonly)
        holder.setVisibility(R.id.tv_hint, !this.hint.isNullOrEmpty() && !viewonly)
        holder.setText(R.id.tv_hint, this.hint)
        holder.setText(R.id.tv_value, checkLabel)
    }

    override fun getTitleLayoutWidth(holder: FormViewHolder): Int {
        return holder.findTitleLayout().measuredWidth
    }

    protected open fun onCheckedChanged(radioGroup: CompoundButton, isChecked: Boolean) {
        onValueChanged(isChecked.toString())
    }

    override fun onClick(view: View) {
        val tag = view.tag
        if (tag is CheckBox) tag.isChecked = !checked
    }
}