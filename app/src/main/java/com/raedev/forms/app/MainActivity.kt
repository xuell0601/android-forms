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
import com.raedev.forms.filter.FormValueFilter
import com.raedev.forms.items.FormItem
import com.raedev.forms.render.DataBindingFormRender
import com.raedev.forms.view.FormItemDecoration
import com.raedev.forms.view.FormLayoutManager

class MainActivity : AppCompatActivity() {

    private val adapter = FormGroupAdapter()

    private val formGroup: FormGroup
        get() = adapter.formGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val start = System.currentTimeMillis()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = FormLayoutManager(this, formGroup)
//        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(FormItemDecoration(this))
        recyclerView.adapter = adapter


        // 自动渲染到表单里面去
        val render = DataBindingFormRender(formGroup, supportFragmentManager)
        baseFormDemo(render)
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
            val result = formGroup.validate()
            if (result.successfully()) {
                Toast.makeText(this, "校验成功", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val text = "表单校验失败：${result.message()}，表单所在位置：${result.formItem!!.position}"
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            Log.e("rae", text)
        }
    }

    private fun baseFormDemo(render: DataBindingFormRender) {
        render.addItem(render.newGroupTitle("表单基础功能演示"))
        render.addItem(render.newNumberEditText("表单过滤器", "filter", null, false).apply {
            this.hint = "输入666拦截"
            object : FormValueFilter {
                override fun filter(item: FormItem, value: String?): String? {
                    if (value == "666") {
                        Toast.makeText(this@MainActivity, "触发拦截", Toast.LENGTH_SHORT).show()
                        return null
                    }
                    return value
                }
            }.also { this.filter = it }
        })
    }

    private fun otherDemo(render: DataBindingFormRender) {
        render.addItem(render.newGroupTitle("自定义添加表单演示"))
        for (i in 0 until 100) {
            val item = render.newEditText("测试表单$i", "TEST$i", i.toString(), i < 10)
            render.addItem(item)
        }
    }

    private fun dataBindingDemo(render: DataBindingFormRender) {
        render.addItem(render.newGroupTitle("自动绑定表单演示"))
        val entity = DemoEntity().apply {
            this.age = 18
        }
        render.bindEntity(entity)
        render.render()
    }
}