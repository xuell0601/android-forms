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
import com.raedev.forms.render.BindingFormRender
import com.raedev.forms.view.FormItemDecoration
import com.raedev.forms.view.FormLayoutManager

class MainActivity : AppCompatActivity() {

    private val adapter = FormGroupAdapter()

    private val formGroup: FormGroup
        get() = adapter.formGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = FormLayoutManager(this)
        recyclerView.addItemDecoration(FormItemDecoration(this))
        recyclerView.adapter = adapter
        val entity = DemoEntity().apply {
            this.name = "张三"
        }
        // 自动渲染到表单里面去
        BindingFormRender(entity, formGroup, supportFragmentManager).render()

        findViewById<Button>(R.id.btn_entity).setOnClickListener {
            val text = "实体值：${Gson().toJson(entity)}"
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            Log.d("rae", text)
        }

        findViewById<Button>(R.id.btn_form).setOnClickListener {
            val text = "表单值：${formGroup.toMap()}"
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            Log.d("rae", text)
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
}