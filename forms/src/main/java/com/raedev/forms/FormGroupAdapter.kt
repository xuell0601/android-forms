package com.raedev.forms

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raedev.forms.internal.FormAdapterProxy
import com.raedev.forms.items.FormItem
import com.raedev.forms.listener.FormViewHolder
import java.lang.ref.WeakReference
import kotlin.math.max

/**
 * RecycleView 表单适配器
 * @author RAE
 * @date 2022/09/02
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class FormGroupAdapter(val formGroup: FormGroup = FormGroup()) :
    RecyclerView.Adapter<FormViewHolder>(), FormAdapterProxy {

    companion object {
        private const val TAG = "RAE.FormGroupAdapter"
    }

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
        formGroup.attachAdapter(this)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        formGroup.detachAdapter()
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

    // region 代理接口实现

    /**
     * 是否可以刷新列表
     */
    private fun canRefresh(): Boolean {
        val recyclerView = getRecyclerView() ?: return false
        if (recyclerView.isComputingLayout) return false
        if (recyclerView.layoutManager?.isSmoothScrolling == true) return false
        return true
    }

    override fun getRecyclerView(): RecyclerView? {
        return recyclerViewRef?.get()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onNeedRefresh() {
        if (canRefresh()) notifyDataSetChanged()
        Log.d(TAG, "onNeedRefresh ")
    }

    override fun onFormItemInserted(insertItem: FormItem?, index: Int) {
        if (!canRefresh()) return
//        Log.d(TAG, "onFormItemInserted:$index")
        notifyItemInserted(index)
    }

    override fun onFormItemRemoved(removeItem: FormItem?, index: Int, count: Int) {
        if (!canRefresh()) return
        if (count > 0) notifyItemRangeRemoved(index, count) else notifyItemRemoved(index)
        Log.d(TAG, "onFormItemRemoved:$index ")
    }

    override fun onFormItemUpdated(index: Int) {
        if (!canRefresh()) return
        this.notifyItemChanged(index)
        Log.d(TAG, "onFormItemUpdated:$index ")
    }

    override fun onFormError(message: String) {
        Log.d(TAG, message)
    }

    override fun refreshVisibleItems() {
        if (!canRefresh()) return
        val layoutManager = getRecyclerView()!!.layoutManager as LinearLayoutManager
        val first = layoutManager.findFirstVisibleItemPosition()
        val last = layoutManager.findLastVisibleItemPosition()
        val count = max(1, last - first)
        notifyItemRangeChanged(first, count)
        Log.d(TAG, "refreshVisibleItems")
    }

    // endregion
}