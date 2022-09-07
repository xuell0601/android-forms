package com.raedev.forms.view

import android.content.Context
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 表单布局管理，主要作用是对表单项进行排列对齐。
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class FormLayoutManager(context: Context?) : LinearLayoutManager(context) {

    /** 布局对齐方式，目前仅支持[Gravity.START]、[Gravity.END]*/
    var gravity: Int = Gravity.START

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
    }
}