package com.raedev.forms.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup

/**
 * RadioGroup 重复点击处理
 * @author RAE
 * @date 2021/03/18
 */
class FormRadioGroup : RadioGroup {

    private var currentCheckedId = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        if (child is RadioButton) {
            child.setOnClickListener { view: View -> onItemClick(view) }
        }
    }

    override fun check(id: Int) {
        super.check(id)
        currentCheckedId = id
    }

    private fun onItemClick(view: View) {
        val button = view as RadioButton
        if (currentCheckedId == button.id) {
            // 重复点击取消选择
            clearCheck()
            return
        }
        currentCheckedId = button.id
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        for (i in 0 until childCount) {
            getChildAt(i).isEnabled = enabled
        }
    }
}