package com.raedev.forms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raedev.forms.listener.FormViewHolder
import java.lang.ref.WeakReference

/**
 * RecycleView 表单适配器
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class FormGroupAdapter(val formGroup: FormGroup = FormGroup()) :
    RecyclerView.Adapter<FormViewHolder>() {

    companion object {
        private const val TAG = "RAE.FormGroupAdapter"
    }

    internal val recyclerView: RecyclerView?
        get() = recyclerViewRef?.get()

    /** viewType和索引之间的对应关系 */
    private val viewTypePositionMap = mutableMapOf<Int, Int>()
    private lateinit var inflater: LayoutInflater
    private var recyclerViewRef: WeakReference<RecyclerView>? = null

    override fun getItemCount(): Int {
        return formGroup.itemCount
    }

    override fun getItemViewType(position: Int): Int {
        val item = formGroup[position]
        viewTypePositionMap[item.formType] = position
        return item.formType
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        inflater = LayoutInflater.from(recyclerView.context)
        recyclerViewRef = WeakReference(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerViewRef?.clear()
        recyclerViewRef = null
        formGroup.destroy()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        val position = viewTypePositionMap[viewType] ?: 0
        val item = formGroup[position]
        return item.onCreateViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val item = formGroup[position]
        item.onBindViewHolderOnAdapter(holder)
    }

    override fun onViewRecycled(holder: FormViewHolder) {
        super.onViewRecycled(holder)
        val position = holder.adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            formGroup[position].onRecycled(holder)
        }
    }

    override fun onViewAttachedToWindow(holder: FormViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            formGroup[position].onViewAttachedToWindow(holder)
        }
    }

    override fun onViewDetachedFromWindow(holder: FormViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val position = holder.adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            formGroup[position].onViewDetachedFromWindow(holder)
            holder.clear()
        }
    }
}