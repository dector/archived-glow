package io.github.dector.glow.core.path

internal fun String.cleanupTitleForWebPath() = this
    .trim()
    .replace(Regex("/+"), "-")
    .replace(Regex(" +"), "-")
    .replace(Regex("[^\\w_-]"), "")
    .replace(Regex("--+"), "-")
    .trim('-')
    .toLowerCase()
