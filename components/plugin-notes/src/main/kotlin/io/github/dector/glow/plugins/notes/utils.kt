package io.github.dector.glow.plugins.notes

internal fun String.cleanupTitleForWebPath() = this
    .trim()
    .replace(Regex("/+"), "-")
    .replace(Regex(" +"), "-")
    .replace(Regex("[^\\w_-]"), "")
    .replace(Regex("--+"), "-")
    .trim('-')
    .toLowerCase()
