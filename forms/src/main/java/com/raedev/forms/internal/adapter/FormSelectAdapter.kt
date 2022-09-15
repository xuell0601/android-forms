package com.raedev.forms.internal.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raedev.forms.R
import com.raedev.forms.dict.FormDataProvider
import com.raedev.forms.dict.FormSelectItem
import com.raedev.forms.view.FormSelectViewHolder

/**
 * 弹出选择对话框表单项的适配器
 * @author RAE
 * @date 2022/09/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@SuppressLint("NotifyDataSetChanged")
internal class FormSelectAdapter(
    private val selectedItem: FormSelectItem?,
    private val provider: FormDataProvider,
    private var listener: ((FormSelectItem) -> Unit)
) : RecyclerView.Adapter<FormSelectViewHolder>(), View.OnClickListener {

    companion object {
        private const val NAVIGATION_NAME = "FROM_NAVIGATION_NAME"
    }

    /** 数据展示的列表 */
    private val items = mutableListOf<FormSelectItem>()

    /** 数量 */
    override fun getItemCount(): Int = items.size

    /** 导航Item */
    private val navigationItem by lazy { FormSelectItem("", NAVIGATION_NAME) }

    init {
        this.performSelectItem(selectedItem)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormSelectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FormSelectViewHolder(
            inflater.inflate(R.layout.form_select_dialog_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FormSelectViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.setOnClickListener(this)
        holder.setLabel(item.label)
        holder.showArrow(item.hasChildren)
        holder.itemView.isSelected = item == selectedItem || item.isNavigation()
    }

    override fun onClick(view: View) {
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        if (position == RecyclerView.NO_POSITION) return
        val item = items[position]
        // 导航栏
        if (item.isNavigation()) {
            // 返回上一级
            goPreLevel(item)
            return
        }
        if (!item.hasChildren) {
            // 没有子项的时候回调
            listener.invoke(item)
            return
        }
        // 进入下一级
        goNextLevel(item)
    }

    /**
     * 前往下一级
     */
    private fun goNextLevel(item: FormSelectItem) {
        items.clear()
        items.addAll(item.children!!)
        items.add(0, makeNavigationItem(item))
        notifyDataSetChanged()
    }

    /**
     * 返回上一级
     */
    private fun goPreLevel(item: FormSelectItem) {
        if (item.parent?.parent == null) {
            // 已经到顶级
            this.performSelectItem(null)
            return
        }
        items.clear()
        items.addAll(item.parent!!.children!!)
        items.add(0, makeNavigationItem(item.parent!!))
        notifyDataSetChanged()
    }

    /**
     * 立即执行选择项
     */
    private fun performSelectItem(item: FormSelectItem?) {
        val data = provider.getItems(item)
        this.items.clear()
        this.items.addAll(data)
        // 处理导航栏
        if (item?.parent != null) {
            // 返回上一级
            items.add(0, makeNavigationItem(item))
        }
        notifyDataSetChanged()
    }

    /**
     * 创建导航栏
     */
    private fun makeNavigationItem(current: FormSelectItem): FormSelectItem {
        navigationItem.label = "返回上一级：" + provider.getNavigationTitle(current)
        navigationItem.parent = current.parent
        return navigationItem
    }

    /**
     * 是否为导航栏
     */
    private fun FormSelectItem.isNavigation(): Boolean {
        return NAVIGATION_NAME == this.value
    }

}