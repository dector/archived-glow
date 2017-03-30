package io.github.dector.glow.renderer

import io.github.dector.glow.models.PageModel
import java.time.LocalDate

interface IRenderer {

    fun render(pageType: PageType, model: PageModel): String
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