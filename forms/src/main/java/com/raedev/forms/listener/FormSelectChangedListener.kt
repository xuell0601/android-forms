package com.raedev.forms.listener

import com.raedev.forms.dict.FormSelectItem

/**
 * 表单选择框回调
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface FormSelectChangedListener {

    /**
     * 选择项发生改变
     * @param item 选项
     */
    fun onFormSelectChanged(item: FormSelectItem)
}