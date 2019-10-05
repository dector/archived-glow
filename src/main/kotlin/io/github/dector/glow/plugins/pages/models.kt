package io.github.dector.glow.plugins.pages

import io.github.dector.glow.core.HtmlContent
import io.github.dector.glow.core.MarkdownContent
import io.github.dector.glow.core.WebPagePath
import java.io.File
import java.time.Instant

data class PageInfo(
        val id: String,
        val title: String,
        val sourceFile: File,
        val isDraft: Boolean,
        val isSection: Boolean
)

data class Page2(
    val title: String,
    val createdAt: Instant?,
    val sourceFile: File,
    val content: MarkdownContent,
    val isSection: Boolean
)

data class Page2VM(
    val title: String,
    val createdAt: Instant?,
    val path: WebPagePath,
    val content: HtmlContent
)
