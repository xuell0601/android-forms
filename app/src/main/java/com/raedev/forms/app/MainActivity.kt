package com.raedev.forms.app

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.raedev.forms.FormGroup
import com.raedev.forms.FormGroupAdapter
import com.raedev.forms.app.entity.DemoEntity
import com.raedev.forms.dict.FormSelectItem
import com.raedev.forms.dict.SimpleDataProvider
import com.raedev.forms.items.RadioGroupFormItem
import com.raedev.forms.render.DataBindingFormRender
import com.raedev.forms.view.FormItemDecoration
import com.raedev.forms.view.FormLayoutManager
import java.util.*

class MainActivity : AppCompatActivity() {

    private val adapter = FormGroupAdapter()

    private val formGroup: FormGroup
        get() = adapter.formGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = FormLayoutManager(this, formGroup)
        recyclerView.addItemDecoration(FormItemDecoration(this))
        recyclerView.adapter = adapter

        // 自动渲染到表单里面去
        val render = DataBindingFormRender(this, adapter, supportFragmentManager)
        baseFormDemo(render)
        childrenDemo(render)
        // 自动绑定
        dataBindingDemo(render)
        // 自定义添加
        otherDemo(render)


        // 按钮事件监听
        findViewById<Button>(R.id.btn_entity).setOnClickListener {
            val text = "实体值：${Gson().toJson(render.entity)}"
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            Log.d("rae", text)
        }

        findViewById<Button>(R.id.btn_form).setOnClickListener {
            val text = "表单值：${formGroup.toMap()}"
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            Log.d("rae", text)
        }
        findViewById<Button>(R.id.btn_viewonly).setOnClickListener {
            formGroup.viewonly = !formGroup.viewonly
            it as Button
            it.text = if (formGroup.viewonly) "可编辑" else "只读"
        }

        findViewById<Button>(R.id.btn_validate).setOnClickListener {
            val result = formGroup.validateForm()
            if (result.successfully()) {
                Toast.makeText(this, "校验成功", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val text = "表单校验失败：${result.message}"
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            Log.e("rae", text)
        }
    }

    private fun childrenDemo(render: DataBindingFormRender) {
        render.addGroupTitle("多级子表单演示")
        val rootItem = render.addRadioGroup("一级表单（试试点击开）", "parent1", false.toString(), true)
        rootItem.setCheckBoxLabel("开", "关")
        rootItem.setFormChangedListener { item, _, _ ->
            item as RadioGroupFormItem
            Log.d("RAE", "一级表单发生改变：$item")
            // 移除所有子项目
            item.removeAllChildren()
            if (item.checked != true) return@setFormChangedListener
            // 二级表单
            render.addEditTextChild(item, "1.1表单", "parent-node1")
            render.addEditTextChild(item, "1.2表单", "parent-node2")
            render.addEditTextChild(item, "1.3表单", "parent-node3")
            // 三级表单
            render.addRadioGroupChild(item, "1.4表单", "parent-node4", null).apply {
                this.setFormChangedListener { subItem, value, _ ->
                    // 再添加子表单
                    subItem.removeAllChildren()
                    if ("true" == value) {
                        render.addEditTextChild(subItem, "1.4.1表单", "parent-node4-node1", null)
                    }
                }
            }
        }
    }

    private fun baseFormDemo(render: DataBindingFormRender) {
        val etName = "ET"
//        render.addGroupTitle("输入框示例")
//        render.addEditText("普通文本", etName.random(), "hello")
//        render.addEditText("只读文本", etName.random(), "word").apply { viewonly = true }
//        render.addMultiEditText("多行文本", etName.random(), "hello")
//        render.addNumberEditText("输入纯数字", etName.random())
//        render.addNumberEditText("输入面积（自动保留2位）", etName.random())
//        render.addNumberEditText("手机号码（自动11位）", etName.random(), required = true)
//        render.addNumberEditText("限制长度", etName.random()).apply {
//            this.hint = "最大长度为6"
//            this.maxLength = 6
//        }
//
//        render.addNumberEditText("表单过滤器", etName.random()).apply {
//            this.hint = "输入666拦截"
//            this.filter = object : FormValueFilter {
//                override fun filter(item: FormItem, value: String?): String? {
//                    if (value == "666") {
//                        Toast.makeText(this@MainActivity, "触发拦截:$value", Toast.LENGTH_SHORT).show()
//                        return null
//                    }
//                    return value
//                }
//            }
//        }
//
//
//        render.addGroupTitle("单选框示例")
//        render.addRadioGroup("单选框1", "radio1", null, false)
//        render.addRadioGroup("单选框2", "radio2", true.toString(), false).apply {
//            this.hint = "默认值为TRUE"
//            this.setCheckBoxLabel("真", "假")
//        }


//        render.addGroupTitle("日期示例")
//        render.addDate("普通日期", randomName())
//        render.addDate("初始化日期", randomName(), "2022-10-01")
//        render.addDate("不同样式的日期", randomName())
//        render.addDate("指定日期（20220901~20221001）", randomName()).apply {
//            this.startDate = "2022-09-01"
//            this.endDate = "2022-10-01"
//        }

        // 数据提供
        val data = mutableListOf<FormSelectItem>().apply {
            for (i in 0 until 20) {
                val item = FormSelectItem("演示项$i", "parent$i", order = -1)
                if (i % 2 == 0) {
                    // 添加子项
                    for (k in 0 until 5) {
                        item.addChild(FormSelectItem("演示项${i}-子项$k", "node$i-$k", order = k))
                    }
                }
                this.add(item)
            }
            val item = FormSelectItem("多级演示项", "parent")
            val sub1 = FormSelectItem("二级选项1", "sub1")
            val sub2 = FormSelectItem("二级选项2", "sub2")
            val three1 = FormSelectItem("三级选项1", "three1")
            val three2 = FormSelectItem("三级选项2", "three2")
            sub1.addChild(three1)
            sub1.addChild(three2)
            item.addChild(sub1)
            item.addChild(sub2)
            this.add(item)
        }

        render.addGroupTitle("选择框示例")
        render.addSelect(
            SimpleDataProvider(data),
            "简单的选择框",
            randomName(),
            null,
            dialogTitle = "我是自定义标题"
        )
        render.addSelect(SimpleDataProvider(data), "自动选择一级", randomName(), "parent1")
        render.addSelect(SimpleDataProvider(data), "自动选择二级", randomName(), "node2-2")
        render.addSelect(SimpleDataProvider(data), "自动选择三级", randomName(), "three2")
    }

    private fun otherDemo(render: DataBindingFormRender) {
        render.addGroupTitle("自定义添加表单演示")
        for (i in 0 until 100) {
            render.addEditText("测试表单$i", "TEST$i", i.toString(), i < 10)
        }
    }

    private fun dataBindingDemo(render: DataBindingFormRender) {
        render.addGroupTitle("自动绑定表单演示")
        val entity = DemoEntity().apply {
            this.age = 18
        }
        render.bindEntity(entity)
        render.render()
    }


    /**
     * 随机生成表单名
     */
    private fun randomName(prefix: String? = ""): String {
        return prefix + "-" + UUID.randomUUID().toString()
    }


    private fun String.random(): String {
        return randomName(this)
    }
}