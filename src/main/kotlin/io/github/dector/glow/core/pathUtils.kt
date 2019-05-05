package io.github.dector.glow.core


internal fun String.simplifyForWebPath() = this
        .replace(" ", "-")
        .replace(Regex("[^\\w_-]"), "")
        .toLowerCase()