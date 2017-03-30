package io.github.dector.glow.builder.renderer

import io.github.dector.glow.builder.models.PageData
import java.time.LocalDate

interface IRenderer {

    fun render(pageType: PageType, data: PageData): String
}

interface IRenderFormatter {
    fun formatPubDate(date: LocalDate?): String
    fun formatPubDateHint(date: LocalDate?): String
}

enum class PageType(val filename: String) {
    Index(filename = "index"),
    Post(filename = "post"),
    Archive(filename = "archive")
}