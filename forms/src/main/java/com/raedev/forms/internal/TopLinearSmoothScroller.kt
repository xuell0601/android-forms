package com.raedev.forms.internal

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * 滚动到顶部
 * @author RAE
 * @date 2022/09/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class TopLinearSmoothScroller(context: Context) : LinearSmoothScroller(context) {


    /** 是否执行动画 */
    var enableAnimation = true

    private val anim by lazy { FormBackgroundAnimation() }


    /**
     * 滚动到顶部
     */
    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_ANY
    }

    /**
     * 开始滚动
     */
    fun scrollNow(recyclerView: RecyclerView, position: Int) {
        this.targetPosition = position
        recyclerView.layoutManager?.startSmoothScroll(this)
    }

    override fun onStop() {
        super.onStop()
        if (enableAnimation) {
            this.findViewByPosition(targetPosition)?.let { anim.start(it) }
        }
    }
}