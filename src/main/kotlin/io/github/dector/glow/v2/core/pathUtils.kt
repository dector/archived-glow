package io.github.dector.glow.v2.core


internal fun String.simplifyForWebPath() = this
        .replace(" ", "-")
        .replace(Regex("[^\\w_-]"), "")
        .toLowerCase()