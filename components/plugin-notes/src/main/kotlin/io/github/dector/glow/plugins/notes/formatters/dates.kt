package io.github.dector.glow.plugins.notes.formatters

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


fun formatPublishDate(instant: Instant?): String {
    instant ?: return ""

    return DateTimeFormatter.ofPattern("E, dd MMM uuuu")
        .withZone(ZoneOffset.UTC)
        .format(instant)
}

fun Instant.formatAsMidDateTime(): String = DateTimeFormatter.ofPattern("E, dd MMM uuuu 'at' HH:mm")
    .withZone(ZoneOffset.UTC)
    .format(this)
