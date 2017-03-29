package io.github.dector.glow.renderer

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DefaultRenderFormatter : IRenderFormatter {

    override fun formatPubDate(date: LocalDate?): String {
        date ?: return ""

        return DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
                .format(date)
    }

    override fun formatPubDateHint(date: LocalDate?): String {
        date ?: return ""

        return DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale.ENGLISH)
                .format(date)
    }
}