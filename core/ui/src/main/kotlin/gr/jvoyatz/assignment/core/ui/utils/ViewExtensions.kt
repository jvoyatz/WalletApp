package gr.jvoyatz.assignment.core.ui.utils

import android.animation.Animator
import android.view.View
import androidx.core.view.isVisible

private const val ALPHA_START = 0f
private const val ALPHA_END = 1f

private const val TRANSLATION_Y_100 = 100f
private const val TRANSLATION_Y_150 = 150f
private const val TRANSLATION_Y_200 = 200f
private const val TRANSLATION_Y_500 = 500f
private const val TRANSLATION_Y_100_MINUS = -100f
private const val TRANSLATION_Y_0 = 0f
private const val DURATION_500 = 500L

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
        .setListener(object : Animator.AnimatorListener {
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
