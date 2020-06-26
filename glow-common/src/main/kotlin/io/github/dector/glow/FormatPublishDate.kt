package io.github.dector.glow

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun formatPublishDate(instant: Instant?): String {
    instant ?: return ""

    return DateTimeFormatter.ofPattern("E, dd MMM uuuu")
        .withZone(ZoneOffset.UTC)
        .format(instant)
}
