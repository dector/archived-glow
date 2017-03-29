package io.github.dector.glow

import org.slf4j.Logger

inline fun assert(rule: String = "", logger: Logger, predicate: () -> Boolean?): Boolean? {
    val result = if (predicate() ?: false) true else null

    if (result.isNullOrFalse() && !rule.isEmpty())
        logger.error("Assertion rule failed: [$rule]")

    return result
}

@Suppress("NOTHING_TO_INLINE")
inline fun Boolean?.isNullOrFalse(): Boolean = this == null || !this

@Suppress("NOTHING_TO_INLINE")
inline fun Boolean?.isTrue(): Boolean = this != null && this