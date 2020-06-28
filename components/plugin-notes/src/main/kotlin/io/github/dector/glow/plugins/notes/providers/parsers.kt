package io.github.dector.glow.plugins.notes.providers

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


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

internal fun String.cleanupTitleForWebPath() = this
    .trim()
    .replace(Regex("/+"), "-")
    .replace(Regex(" +"), "-")
    .replace(Regex("[^\\w_-]"), "")
    .replace(Regex("--+"), "-")
    .trim('-')
    .toLowerCase()
