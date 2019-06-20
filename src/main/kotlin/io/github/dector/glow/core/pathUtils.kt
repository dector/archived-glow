package io.github.dector.glow.core

internal fun String.simplifyForWebPath() = this
    .trim()
    .replace(Regex("/+"), "-")
    .replace(Regex(" +"), "-")
    .replace(Regex("[^\\w_-]"), "")
    .trim('-')
    .toLowerCase()
