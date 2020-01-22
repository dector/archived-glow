package io.github.dector.glow.utils

inline fun <T : Any?> T.transformIf(predicate: Boolean, transformer: (T) -> T): T =
    if (predicate) transformer(this) else this

inline fun <T : List<R>, R : Any?> T.takeIfNotEmpty(): T? =
    takeIf { it.isNotEmpty() }
