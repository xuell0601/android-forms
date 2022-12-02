package com.raedev.forms.render

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.raedev.forms.FormGroupAdapter
import com.raedev.forms.FormInputType
import com.raedev.forms.FormType
import com.raedev.forms.dict.FormDataProvider
import com.raedev.forms.internal.mapping.FormFieldMap
import com.raedev.forms.internal.mapping.FormFieldMapping
import com.raedev.forms.internal.mapping.ReflectionFieldMapping
import com.raedev.forms.items.FormItem
import org.json.JSONObject

/**
 * 实体绑定的表单渲染
 * @author RAE
 * @date 2022/09/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class DataBindingFormRender(
    context: Context,
    adapter: FormGroupAdapter,
    fragmentManager: FragmentManager? = null
) : FormRender(context, adapter, fragmentManager), IFormDataBinding {

    protected val mapping: FormFieldMapping by lazy { ReflectionFieldMapping() }

    /** 表单列表选择数据提供，建议做成字典表，根据指定的字典Key去筛选数据 */
    var provider: FormDataProvider? = null

    override fun bind(entity: Any) {
        mapping.parse(entity)
    }

    override fun unbind() {
        mapping.clean()
    }

    override fun setFormValue(formItem: FormItem) {
        mapping.setValue(formItem.name, formItem.value)
    }


    override fun render() {
        super.render()
        // 分组
        val groupList = mapping.fields.groupBy { it.group }.toSortedMap { a, b ->
            a.compareTo(b)
        }
        val groupSize = groupList.size
        groupList.forEach { map ->
            val groupName = map.key
            val items = map.value
            // 渲染分组标题
            if (groupSize > 1 && groupName.isNotBlank()) {
                renderGroupTitle(groupName)
            }
            // 排序处理
            val fields = items.sortedBy { it.order }
            fields.forEach {
                if (!it.name.equals(it.label, true)) {
                    renderFormItem(it)
                }
            }
        }
    }

    /**
     * 渲染表单
     */
    protected open fun renderFormItem(item: FormFieldMap): FormItem? {
        return when (item.formType) {
            FormType.GroupTitle -> renderGroupTitle(item.label)
            FormType.EditText -> renderEditText(item)
            FormType.NumberEditText -> renderEditText(item)
            FormType.RadioGroup -> renderRadioGroup(item)
            FormType.CheckBox -> renderCheckBox(item)
            FormType.Date -> renderDate(item)
            FormType.Select -> renderSelect(item)
        }
    }

    protected open fun renderRadioGroup(item: FormFieldMap): FormItem {
        return addRadioGroup(
            item.label,
            item.name,
            item.value?.toString(),
            item.required,
            findParent(item.parentName)
        ).also {
            when {
                item.label.contains("性别") -> it.setCheckBoxLabel("男", "女")
            }
        }
    }


    protected open fun renderCheckBox(item: FormFieldMap): FormItem {
        return addCheckBox(
            item.label,
            item.name,
            item.value?.toString(),
            item.required,
            findParent(item.parentName)
        )
    }

    protected open fun renderDate(item: FormFieldMap): FormItem {
        return addDate(
            item.label,
            item.name,
            item.value?.toString(),
            item.required,
            findParent(item.parentName)
        )
    }

    protected open fun renderSelect(item: FormFieldMap): FormItem {
        val dataProvider = provider ?: throw NullPointerException("请先设置表单选择数据源")
        return addSelect(
            dataProvider,
            item.label,
            item.name,
            item.value?.toString(),
            item.required,
            parent = findParent(item.parentName)
        ).also { it.dictFilter = item.dict }
    }

    protected open fun renderGroupTitle(label: String) = addGroupTitle(label)

    protected open fun renderEditText(item: FormFieldMap): FormItem {
        val parent = findParent(item.parentName)
        val inputType = when (item.formType) {
            // 当为数字类型时候根据Label情景推断具体的输入类型
            FormType.NumberEditText -> when {
                item.label.contains("面积") -> FormInputType.Decimal
                item.label.contains("备注") -> FormInputType.MultiText
                item.label.contains("手机") -> FormInputType.Phone
                else -> FormInputType.Number
            }
            else -> FormInputType.Text
        }
        return addEditTextInternal(
            inputType,
            item.label,
            item.name,
            item.value?.toString(),
            item.required,
            parent
        )
    }

    protected open fun findParent(parentName: String?): FormItem? {
        val parent = parentName ?: return null
        val item = mapping[parent] ?: return null
        return renderFormItem(item)
    }

    override fun toMap(): Map<String, String?> {
        return mutableMapOf<String, String?>().also { map ->
            mapping.fields.forEach {
                map[it.name] = it.value?.toString()
            }
        }
    }

    override fun toJson(): String {
        val obj = JSONObject()
        mapping.fields.forEach {
            obj.put(it.name, it.value)
        }
        return obj.toString()
    }


}