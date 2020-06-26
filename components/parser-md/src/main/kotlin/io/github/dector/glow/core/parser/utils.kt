package io.github.dector.glow.core.parser

import java.io.File
import java.time.Instant


internal fun String.parseInstant() = try {
    Instant.parse(this)
} catch (e: Throwable) {
    null
}

internal fun parseInstant(str: String?, fallback: () -> Instant): Instant {
    str ?: fallback()

    return try {
        Instant.parse(str)
    } catch (e: Throwable) {
        fallback()
    }
}

internal fun markdownFileId(file: File) = file.nameWithoutExtension
        .toLowerCase()
        .replace(" ", "-")
