package io.github.dector.glow

import java.io.File


inline fun <reified T> T.applyIf(predicate: Boolean, block: T.() -> Unit): T =
    if (predicate) apply(block) else this

operator fun File.div(path: String) = resolve(path)
