package io.github.dector.glow.plugins.notes

import io.github.dector.glow.core.HtmlContent
import io.github.dector.glow.core.MarkdownContent
import io.github.dector.glow.core.WebPagePath
import java.io.File
import java.time.Instant

data class NoteInfo(
        val id: String,
        val title: String,
        val isDraft: Boolean,
        val createdAt: Instant,
        val sourceFile: File
)

data class Note2(
        val title: String,
        val createdAt: Instant?,
        val publishedAt: Instant?,
        val isDraft: Boolean,
        val sourceFile: File,
        val content: MarkdownContent
)

data class Note2VM(
        val title: String,
        val createdAt: Instant?,
        val publishedAt: Instant?,
        val path: WebPagePath,
        val content: HtmlContent,
        val previewContent: HtmlContent
)