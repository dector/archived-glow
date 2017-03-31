package io.github.dector.glow.tools

fun <E> List<E>.prevOrNull(item: E): E? {
    val index = indexOf(item)

    return if (index != -1)
        getOrNull(index - 1)
    else null
}

fun <E> List<E>.nextOrNull(item: E): E? {
    val index = indexOf(item)

    return if (index != -1)
        getOrNull(index + 1)
    else null
}