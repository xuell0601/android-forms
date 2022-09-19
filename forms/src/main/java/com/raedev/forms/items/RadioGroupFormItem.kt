package com.raedev.forms.items

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.raedev.forms.FormType
import com.raedev.forms.R
import com.raedev.forms.listener.FormViewHolder

/**
 * 单选框表单
 * @author RAE
 * @date 2022/09/13
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class RadioGroupFormItem(
    label: String, name: String, value: String?,
    required: Boolean = false
) : FormItem(label, name, value) {

    override val layoutId: Int = R.layout.form_item_radio

    override val formType: Int = FormType.RadioGroup.ordinal

    protected open val checkLabels = arrayOf("是", "否")

    override var hint: String? = null

    /** 关联的表单名 */
    var relationFormName: String? = null

    init {
        this.required = required
    }


    /**
     * 设置选中状态
     */
    open var checked: Boolean?
        get() = if (this.value == null) null else "true".equals(this.value, true)
        set(value) {
            this.value = value.toString()
        }

    protected open val checkLabel: String
        get() = when (checked) {
            true -> checkLabels[0]
            false -> checkLabels[1]
            else -> ""
        }

    protected open fun FormViewHolder.radioGroup(): RadioGroup {
        return this.getView(R.id.rg_value)
    }

    protected open fun FormViewHolder.checkBox(): RadioButton {
        return this.getView(R.id.rb_checked)
    }

    protected open fun FormViewHolder.uncheckBox(): RadioButton {
        return this.getView(R.id.rb_unchecked)
    }


    override fun onBindViewEvent(holder: FormViewHolder) {
        super.onBindViewEvent(holder)
        holder.radioGroup().setOnCheckedChangeListener(this::onCheckedChanged)
    }

    override fun onUnBindViewEvent(holder: FormViewHolder) {
        holder.radioGroup().setOnCheckedChangeListener(null)
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
        holder.setVisibility(R.id.tv_hint, !this.hint.isNullOrEmpty())
        holder.setText(R.id.tv_hint, this.hint)
        holder.checkBox().text = checkLabels[0]
        holder.uncheckBox().text = checkLabels[1]
        val radioGroup = holder.radioGroup()
        radioGroup.isEnabled = this.enabled
        radioGroup.visibility = if (viewonly) View.GONE else View.VISIBLE
        when (this.checked) {
            null -> radioGroup.clearCheck()
            true -> radioGroup.check(R.id.rb_checked)
            false -> radioGroup.check(R.id.rb_unchecked)
        }

        holder.setVisibility(R.id.tv_value, viewonly)
        holder.setVisibility(R.id.tv_hint, !viewonly)
        holder.setText(R.id.tv_value, checkLabel)
    }

    override fun getTitleLayoutWidth(holder: FormViewHolder): Int {
        return holder.findTitleLayout().measuredWidth
    }

    protected open fun onCheckedChanged(radioGroup: RadioGroup, checkId: Int) {
        val isChecked: Boolean? = when (checkId) {
            R.id.rb_checked -> true
            R.id.rb_unchecked -> false
            else -> null
        }
        val value: String? = isChecked?.toString()
        onValueChanged(value)
    }
}