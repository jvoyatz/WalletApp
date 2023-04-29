package gr.jvoyatz.assignment.core.ui.utils

import android.content.res.TypedArray


inline fun <reified T : Enum<T>> TypedArray.getEnumType(index: Int, default: T) {
    return getInt(index, -1).let {
        if (it >= 0) enumValues<T>()[it] else default
    }
}