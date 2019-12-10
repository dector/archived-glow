package io.github.dector.glow.utils

inline fun <T : Any?> T.transformIf(predicate: Boolean, transformer: (T) -> T): T =
    if (predicate) transformer(this) else this
