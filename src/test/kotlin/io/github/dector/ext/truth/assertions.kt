package io.github.dector.ext.truth

import com.google.common.truth.Subject


inline fun <reified T : Any> Subject<*, *>.isInstanceOf() {
    return isInstanceOf(T::class.java)
}
