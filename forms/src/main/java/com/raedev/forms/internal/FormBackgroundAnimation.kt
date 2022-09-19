package com.raedev.forms.internal

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import java.lang.ref.WeakReference

/**
 * FormItem背景动画
 * @author RAE
 * @date 2021/03/08
 */
class FormBackgroundAnimation : Animation.AnimationListener {
    /**
     * 高亮颜色
     */
    private val highlightColor: Drawable = ColorDrawable(Color.parseColor("#66FF0000"))
    private val normalColor: Drawable = ColorDrawable(Color.parseColor("#66FFFF00"))
    private val alphaAnimation: AlphaAnimation = AlphaAnimation(1F, 1F)
    private var rawBackground: Drawable? = null
    private var viewRef: WeakReference<View>? = null
    private val animView: View?
        get() = viewRef?.get()

    private var isRunning: Boolean = false

    init {
        alphaAnimation.repeatCount = 5
        alphaAnimation.duration = 150
        alphaAnimation.interpolator = LinearInterpolator()
        alphaAnimation.setAnimationListener(this)
    }

    fun start(view: View?) {
        if (isRunning) return
        view ?: return
        this.viewRef = WeakReference(view)
        rawBackground = view.background
        view.startAnimation(alphaAnimation)
    }

    override fun onAnimationStart(animation: Animation) {
        isRunning = true
    }

    override fun onAnimationEnd(animation: Animation) {
        isRunning = false
        animView?.let { view ->
            view.background = rawBackground
            viewRef?.clear()
        }
    }

    override fun onAnimationRepeat(animation: Animation) {
        animView?.let { view ->
            // 颜色切换
            view.background = when (view.background) {
                highlightColor -> normalColor
                else -> highlightColor
            }
        }
    }

}