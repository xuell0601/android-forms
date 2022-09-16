package com.raedev.forms.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raedev.forms.R
import com.raedev.forms.dict.FormDataProvider
import com.raedev.forms.dict.FormSelectItem
import com.raedev.forms.internal.adapter.FormSelectAdapter
import com.raedev.forms.listener.FormSelectChangedListener

/**
 * 表单选择对话框
 * @author RAE
 * @date 2022/09/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class FormSelectDialogFragment : AppCompatDialogFragment() {

    companion object {

        fun show(
            manager: FragmentManager, selectValue: String?, title: String? = null
        ): FormSelectDialogFragment {
            return FormSelectDialogFragment().apply {
                this.arguments = Bundle().apply {
                    this.putString("value", selectValue)
                    this.putString("title", title)
                }
                this.show(manager, "FormSelectDialog")
            }
        }
    }

    internal lateinit var provider: FormDataProvider
    var listener: FormSelectChangedListener? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AppCompatDialog(requireContext(), R.style.SelectionFormItemDialog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.form_select_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titleView = view.findViewById<TextView>(R.id.tv_title)
        val rv = view.findViewById<RecyclerView>(R.id.recyclerView)
        view.findViewById<ImageView>(R.id.img_close).setOnClickListener { dismiss() }
        arguments?.getString("title")?.let { titleView.text = it }
        val selectedItem = arguments?.getString("value")?.let { provider.getItem(it) }
        rv.adapter = FormSelectAdapter(selectedItem, provider) { onItemClick(it) }
        rv.layoutManager = LinearLayoutManager(view.context)
        rv.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
    }

    private fun onItemClick(item: FormSelectItem) {
        listener?.onFormSelectChanged(item)
        dismiss()
    }

}