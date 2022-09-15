package com.raedev.forms.items

import com.raedev.forms.FormType
import com.raedev.forms.R
import com.raedev.forms.listener.FormViewHolder
import java.util.*

/**
 * 分组标题
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class GroupTitleFormItem(label: String) : FormItem(label, "GT-${UUID.randomUUID()}", null) {

    override val layoutId: Int = R.layout.form_item_group_title
    override val formType: Int = FormType.GroupTitle.ordinal

    override fun onBindViewHolder(holder: FormViewHolder) {
        holder.setText(R.id.tv_title, this.label)
    }

}