package io.github.dector.glow.core.parser

import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


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

internal fun parseUpdatedAt(str: String?): Instant? =
    parseCreatedAt(str)

internal fun parsePublishedAt(str: String?): Instant? =
    parseCreatedAt(str)

internal fun parseCreatedAt(str: String?): Instant? {
    str ?: return null

    var localDate: LocalDate? = null

    try {
        localDate = DateTimeFormatter.ofPattern("d-M-yy")
                .parse(str, LocalDate::from)
    } catch (e: DateTimeParseException) {
    }

    if (localDate == null) {
        try {
            localDate = DateTimeFormatter.ofPattern("d-M-yyyy")
                    .parse(str, LocalDate::from)
        } catch (e: DateTimeParseException) {
        }
    }

    return if (localDate != null) localDate
            .atStartOfDay()
            .toInstant(ZoneOffset.UTC)
    else null
}
