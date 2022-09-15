package com.raedev.forms.dialog

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import java.util.*

/**
 * 日期选择对话框
 * @author RAE
 * @date 2022/09/14
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@SuppressLint("SimpleDateFormat")
class DatePickDialogFragment : AppCompatDialogFragment() {

    var listener: OnDateSetListener? = null
    var minDate: Long? = null
    var maxDate: Long? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        var year = calendar[Calendar.YEAR]
        var month = calendar[Calendar.MONTH]
        var day = calendar[Calendar.DAY_OF_MONTH]
        arguments?.let {
            year = it.getInt("year", year)
            month = it.getInt("month", year)
            day = it.getInt("day", year)
        }
        return DatePickerDialog(requireContext(), listener, year, month, day).apply {
            this@DatePickDialogFragment.minDate?.let {
                this.datePicker.minDate = it
            }
            this@DatePickDialogFragment.maxDate?.let {
                this.datePicker.maxDate = it
            }
        }
    }


    fun setDate(date: Date?) = apply {
        date ?: return this
        val argument = arguments ?: Bundle().also { arguments = it }
        val calendar = Calendar.getInstance()
        calendar.time = date
        argument.putInt("year", calendar[Calendar.YEAR])
        argument.putInt("month", calendar[Calendar.MONTH])
        argument.putInt("day", calendar[Calendar.DAY_OF_MONTH])
    }
}