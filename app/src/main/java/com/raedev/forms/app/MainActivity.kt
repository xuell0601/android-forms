package com.raedev.forms.app

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.raedev.forms.FormGroup
import com.raedev.forms.FormGroupAdapter
import com.raedev.forms.app.entity.DemoEntity
import com.raedev.forms.dict.FormSelectItem
import com.raedev.forms.dict.SimpleDataProvider
import com.raedev.forms.filter.FormValueFilter
import com.raedev.forms.items.CheckBoxFormItem
import com.raedev.forms.items.FormItem
import com.raedev.forms.render.DataBindingFormRender
import com.raedev.forms.view.FormItemDecoration
import com.raedev.forms.view.FormLayoutManager
import java.util.*

class MainActivity : AppCompatActivity() {

    private val adapter = FormGroupAdapter()
    lateinit var render: DataBindingFormRender

    private val formGroup: FormGroup
        get() = adapter.formGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = FormLayoutManager(this, formGroup)
        recyclerView.addItemDecoration(
            FormItemDecoration(
                this, ContextCompat.getColor(
                    this,
                    com.raedev.forms.R.color.form_hint_primary
                )
            ).apply {
                height = 2
                paddingBottom = 200
            }
        )
        // 初始化表单渲染器
        this.render = DataBindingFormRender(this, adapter, supportFragmentManager)
        // 初始化示例
        dataBindingDemo()
        editTextDemo()
        radioDemo()
        dateDemo()
        selectDemo()
        parentAndChildrenDemo()
        otherDemo()


        // 按钮事件监听
        findViewById<Button>(R.id.btn_entity).setOnClickListener {
            val text = "实体值：${render.toJson()}"
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


    /**
     * 文本编辑框示例
     */
    private fun editTextDemo() {
        val etName = "ET"
        render.addGroupTitle("输入框示例")
        render.addEditText("普通文本", etName.random(), "hello")
        render.addEditText("只读文本", etName.random(), "word").apply { viewonly = true }
        render.addMultiEditText("多行文本", etName.random(), "多行文本")
        render.addNumberEditText("输入纯数字", etName.random())
        render.addNumberEditText("输入面积（自动保留2位）", etName.random())
        render.addNumberEditText("手机号码（自动11位）", etName.random(), required = true)
        render.addNumberEditText("限制长度", etName.random()).apply {
            this.hint = "最大长度为6"
            this.maxLength = 6
        }
        render.addNumberEditText("表单过滤器", etName.random()).apply {
            this.hint = "输入666拦截"
            this.filter = object : FormValueFilter {
                override fun filter(item: FormItem, value: String?): String? {
                    if (value == "666") {
                        Toast.makeText(this@MainActivity, "触发拦截:$value", Toast.LENGTH_SHORT).show()
                        return null
                    }
                    return value
                }
            }
        }
    }

    /**
     * 单选框示例
     */
    private fun radioDemo() {
        render.addGroupTitle("单选框示例")
        render.addRadioGroup("单选框1", "radio1", null, false)
        render.addRadioGroup("单选框2", "radio2", true.toString(), false).apply {
            this.hint = "默认值为TRUE"
            this.setCheckBoxLabel("真", "假")
        }
    }

    /**
     * 日期示例
     */
    private fun dateDemo() {
        render.addGroupTitle("日期示例")
        render.addDate("普通日期", randomName())
        render.addDate("初始化日期", randomName(), "2022-10-01")
        render.addDate("不同样式的日期", randomName())
        render.addDate("指定日期（20220901~20221001）", randomName()).apply {
            this.startDate = "2022-09-01"
            this.endDate = "2022-10-01"
        }
    }

    /**
     * 选择框示例
     */
    private fun selectDemo() {
        // 初始化数据提供
        val data = mutableListOf<FormSelectItem>().apply {
            for (i in 0 until 20) {
                val item = FormSelectItem("演示项$i", "parent$i", order = -1)
                this.add(item)
                if (i < 2) {
                    // 添加子项
                    for (k in 0 until 5) {
                        item.addChild(FormSelectItem("演示项${i}-子项$k", "node$i-$k", order = k))
                    }
                }
            }

            val item = FormSelectItem("多级演示项", "parent").apply {
                addChild(FormSelectItem("二级选项1", "level2-1"))
                addChild(FormSelectItem("二级选项2", "level2-2"))
                addChild(FormSelectItem("二级选项3", "level2-3").apply {
                    addChild(FormSelectItem("三级选项1", "level3-1"))
                    addChild(
                        FormSelectItem(
                            "三级选项222222222222333333333333333444444444",
                            "level3-2"
                        ).apply {
                            addChild(FormSelectItem("四级选项1", "level4-1"))
                            addChild(FormSelectItem("四级选项2", "level4-2"))
                            addChild(FormSelectItem("四级选项3", "level4-3"))
                            addChild(FormSelectItem("四级选项3", "level4-4"))
                        })
                    addChild(FormSelectItem("三级选项3", "level3-3"))
                    addChild(FormSelectItem("三级选项4", "level3-4"))
                })
            }
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
        render.addSelect(SimpleDataProvider(data), "自动选择演示项5", randomName(), "parent5")
        render.addSelect(
            SimpleDataProvider(data),
            "自动选择二级",
            randomName(),
            "node1-2"
        )
        render.addSelect(
            SimpleDataProvider(data).apply {
                pathSeparator = "-"
                enableCheckParent = true
            },
            "自动选择三级",
            randomName(),
            "level3-1"
        )
    }

    /**
     * 父子表单示例关联示例
     */
    private fun parentAndChildrenDemo() {
        render.addGroupTitle("表单关联示例")
        val rootItem = render.addCheckBox("表单关联", "parent1", false.toString(), true).apply {
            hint = "打开试试看吧"
            setCheckBoxLabel("开", "关")
        }
        rootItem.setFormChangedListener { item, _, _ ->
            // 移除所有子项目
            item.removeAllChildren()
            if (!(item as CheckBoxFormItem).checked) return@setFormChangedListener
            // 二级表单
            render.addEditTextChild(item, "1-1表单", "parent-node1", required = true)
            render.addEditTextChild(item, "1-2表单", "parent-node2")
            render.addEditTextChild(item, "1-3表单", "parent-node3")
            // 三级表单
            render.addRadioGroupChild(item, "继续打开关联", "parent-node4", null).apply {
                this.setFormChangedListener { subItem, value, _ ->
                    // 再添加子表单
                    subItem.removeAllChildren()
                    if ("true" == value) {
                        render.addEditTextChild(subItem, "1-4-1表单", "parent-node4-node1", null)
                    }
                }
            }
        }
    }

    private fun dataBindingDemo() {
        render.addGroupTitle("数据绑定示例（按分组展示）")
        val entity = DemoEntity().apply {
            age = 18
            sex = 1
            sfbd = 1
            birthday = "2022-09-01"
            workCity = "BJ"
            lastWorkCity = "GZ"
            testSelection = ""
        }
        val data = mutableListOf<FormSelectItem>().apply {
            add(FormSelectItem("北京", "WORK.BJ"))
            add(FormSelectItem("上海", "WORK.SH"))
            add(FormSelectItem("广州", "WORK.GZ"))
            add(FormSelectItem("深圳", "WORK.SZ"))
            add(FormSelectItem("成都", "WORK.CD"))
            add(FormSelectItem("测试选项1", "TEST.01"))
            add(FormSelectItem("测试选项2", "TEST.02"))
            add(FormSelectItem("测试选项3", "TEST.03"))
        }

        // 字典表
        render.provider = SimpleDataProvider(data)
        render.bind(entity)
        render.render()
    }

    private fun otherDemo() {
        render.addGroupTitle("其他示例")
        for (i in 0 until 10) {
            render.addEditText("测试$i", "test$i")
        }
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

    override fun onDestroy() {
        super.onDestroy()
        render.unbind()
    }
}