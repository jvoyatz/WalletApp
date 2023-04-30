@file:Suppress("unused")

package gr.jvoyatz.assignment.core.ui.utils

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import com.google.android.material.color.MaterialColors

private const val ALPHA_START = 0f
private const val ALPHA_END = 1f

private const val TRANSLATION_Y_100 = 100f
private const val TRANSLATION_Y_150 = 150f
private const val TRANSLATION_Y_200 = 200f
private const val TRANSLATION_Y_500 = 500f
private const val TRANSLATION_Y_100_MINUS = -100f
private const val TRANSLATION_Y_0 = 0f
private const val DURATION_500 = 500L
private const val DURATION_1000 = 1000L

private const val SCALE_1 = 1f
private const val SCALE_1_25 = 1.50f
private const val SCALE_1_50 = 1.50f
private const val SCALE_1_75 = 1.75f


private fun View.animate(
    alphaStart: Float = ALPHA_START,
    translationYStart: Float,
    alphaEnd: Float = ALPHA_END,
    translationYEnd: Float = TRANSLATION_Y_0,
    duration: Long = DURATION_500 ){

    isVisible = true
    alpha = alphaStart
    translationY = translationYStart

    animate()
        .alpha(alphaEnd)
        .translationY(translationYEnd)
        .setDuration(duration)
        .setListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                translationY = translationYEnd
                alpha = alphaEnd
            }

            override fun onAnimationCancel(animation: Animator) {
                translationY = translationYEnd
                alpha = alphaEnd
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        .start()
}

fun View.fromTopAnimation() = animate(translationYStart = TRANSLATION_Y_100_MINUS)
fun View.fromBottomAnimation() = animate( translationYStart = TRANSLATION_Y_150)

fun ImageView.onLargerScaleAnimation(@DrawableRes startDrawable: Int, @DrawableRes endDrawableRes: Int) {
    setImageResource(startDrawable)
    scaleX = SCALE_1_75
    scaleY = SCALE_1_75
    animate()
        .scaleX(SCALE_1)
        .scaleY(SCALE_1)
        .setDuration(DURATION_1000)
        .setListener(object : AnimatorListener{
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                setImageResource(endDrawableRes)
                scaleY = SCALE_1
                scaleX = SCALE_1
            }

            override fun onAnimationCancel(animation: Animator) {
                TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(animation: Animator) {
                TODO("Not yet implemented")
            }

        })
}

fun View.hide() = with(this) {
    if(this.isVisible){
        isVisible = false
    }
}

fun View.show() = with(this) {
    if(!this.isVisible){
        isVisible = true
    }
}

fun TextView.rightDrawable(@DrawableRes id: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, id, 0)
}
fun TextView.bottomDrawable(@DrawableRes id: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, id)
}

fun View.getAttrColor(id: Int) = MaterialColors.getColor(context, id, Color.BLACK)

fun View.getAttrColor(block: () -> Int) = getAttrColor(block())


fun Context?.showToastLong(message: String) = this?.let { Toast.makeText(this, message, Toast.LENGTH_LONG).show() }
fun Context?.showToastShort(message: String) = this?.let { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }