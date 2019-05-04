package io.github.dector.glow.v2.dumbimpl.utils

import io.github.dector.glow.core.utils.nextOrNull


fun <T> List<T>.isLast(item: T) = nextOrNull(item) == null