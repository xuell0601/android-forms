package com.raedev.forms.items

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.view.View
import com.raedev.forms.FormType
import com.raedev.forms.R
import com.raedev.forms.dialog.DatePickDialogFragment
import com.raedev.forms.listener.FormViewHolder
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期选择表单
 * @author RAE
 * @date 2022/09/14
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("MemberVisibilityCanBePrivate")
@SuppressLint("SimpleDateFormat")
class DateFormItem(
    label: String, name: String, value: String?,
    required: Boolean = false,
    dateFormat: String? = null
) : FormItem(label, name, value) {

    companion object {
        const val STYLE_ANDROID: Int = 0
        const val STYLE_SHELL: Int = 1
    }

    override val layoutId: Int = R.layout.form_item_date
    override val formType: Int = FormType.Date.ordinal

    private var format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    /** 日期格式 */
    var dateFormat: String = "yyyy-MM-dd"
        set(value) {
            field = value
            this.format = SimpleDateFormat(value)
        }

    /** 开始日期 */
    var startDate: String? = null

    /** 结束日期 */
    var endDate: String? = null

    /** 日期样式 */
    var style: Int = STYLE_ANDROID

    /** 当前日期时间 */
    private var currentDate: Date? = null

    /** 日期选择回调 */
    private val datePickListener =
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            this.onDateChanged(calendar.time)
        }


    init {
        // 日期格式化
        dateFormat?.let { this.dateFormat = it }
        this.currentDate = value?.let { format.parse(it) }
        this.required = required
    }

    override fun onBindViewEvent(holder: FormViewHolder) {
        super.onBindViewEvent(holder)
        holder.itemView.setOnClickListener(this::onItemClick)
    }

    override fun onUnBindViewEvent(holder: FormViewHolder) {
        holder.itemView.setOnClickListener(null)
    }

    override fun onBindViewHolder(holder: FormViewHolder) {
        debug("当前时间：$currentDate")
        holder.setText(R.id.tv_date, this.value)
    }

    /**
     * 日期发生改变
     */
    private fun onDateChanged(date: Date) {
        currentDate = date
        val value = format.format(date)
        onValueChanged(value)
    }

    /**
     * 点击事件处理
     */
    private fun onItemClick(view: View) {
        showDatePick()
    }

    /**
     * 显示Android的日期选择对话框
     */
    private fun showDatePick() {
        val manager = fragmentManager ?: return
        DatePickDialogFragment()
            .setDate(currentDate)
            .apply {
                this.minDate = startDate?.let { format.parse(it)?.time }
                this.maxDate = endDate?.let { format.parse(it)?.time }
                this.listener = datePickListener
            }
            .show(manager, "DatePickDialogFragment")
    }


}