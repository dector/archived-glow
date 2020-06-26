package io.github.dector.ktx


inline fun <reified T> T.applyIf(predicate: Boolean, block: T.() -> Unit): T =
    if (predicate) apply(block) else this
