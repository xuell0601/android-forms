package com.raedev.forms.internal.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
) : RecyclerView.Adapter<FormSelectViewHolder>(), View.OnClickListener, View.OnLongClickListener {

    companion object {
        private const val NAVIGATION_NAME = "FROM_NAVIGATION_NAME"
    }

    /** 数据展示的列表 */
    private val items = mutableListOf<FormSelectItem>()

    /** 数量 */
    override fun getItemCount(): Int = items.size

    /** 导航Item */
    private val navigationItem by lazy { FormSelectItem("", "", NAVIGATION_NAME) }

    init {
        // 初始化数据
        setDataList(provider.getItems(selectedItem))
        if (selectedItem?.parent != null) {
            appendNavigationItem(selectedItem)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormSelectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FormSelectViewHolder(
            inflater.inflate(R.layout.form_select_dialog_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FormSelectViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.isSelected = item == selectedItem
        holder.itemView.setOnClickListener(this)
        holder.itemView.setOnLongClickListener(this)
        holder.setLabel(item.label)
        holder.showArrow(item.hasChildren)
        holder.showBackArrow(item.isNavigation())
        holder.showCheckView(provider.enableCheckParent && item.isNavigation())
        holder.checkView.tag = position
        holder.checkView.setOnClickListener(this)
    }


    override fun onClick(view: View) {
        if (view.id == R.id.tv_check) {
            val position = view.tag as Int
            onCheckClick(items[position].parent!!)
            return
        }
        val item = findItemByView(view) ?: return
        when {
            // 点击导航栏的时候返回上一级
            item.isNavigation() -> backToThePreviousLevel(item)
            // 有子节点的时候前往下一级
            item.hasChildren -> goToTheNextLevel(item)
            // 其他情况执行回调
            else -> listener.invoke(item)
        }
    }

    /**
     * 点击选择
     */
    private fun onCheckClick(item: FormSelectItem) {
        provider.getItem(item.value)?.let {
            listener.invoke(it)
        }
    }

    /**
     * 返回上一级
     */
    private fun backToThePreviousLevel(item: FormSelectItem) {
        // 回到顶级
        val parent = item.parent!!.parent ?: return setDataList(provider.getItems())
        setDataList(parent.children!!)
        appendNavigationItem(item.parent!!)
    }

    /**
     * 前往下一级
     */
    private fun goToTheNextLevel(item: FormSelectItem) {
        if (!item.hasChildren) return
        val children = item.children ?: return
        setDataList(children)
        appendNavigationItem(children[0])
    }

    /**
     * 重新设置并刷新当前数据列表
     */
    private fun setDataList(data: List<FormSelectItem>) {
        this.items.clear()
        this.items.addAll(data)
        notifyDataSetChanged()
    }

    /**
     * 添加导航栏
     */
    private fun appendNavigationItem(item: FormSelectItem) {
        navigationItem.apply {
            value = item.value
            parent = item.parent
            label = provider.getNavigationTitle(item)
            items.add(0, this)
            notifyItemInserted(0)
        }
    }

    /**
     * 是否为导航栏
     */
    private fun FormSelectItem.isNavigation(): Boolean {
        return NAVIGATION_NAME == this.name
    }

    private fun findItemByView(view: View): FormSelectItem? {
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        if (position == RecyclerView.NO_POSITION) return null
        return items[position]
    }

    override fun onLongClick(v: View): Boolean {
        val item = findItemByView(v) ?: return false
        Toast.makeText(v.context, item.label, Toast.LENGTH_SHORT).show()
        return true
    }

}