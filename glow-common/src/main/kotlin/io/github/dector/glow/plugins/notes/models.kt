package io.github.dector.glow.plugins.notes

import io.github.dector.glow.core.HtmlContent
import io.github.dector.glow.core.MarkdownContent
import io.github.dector.glow.core.WebPagePath
import java.io.File
import java.time.Instant

data class Note(
    val title: String,
    val createdAt: Instant?,
    val publishedAt: Instant?,
    val isDraft: Boolean,
    val sourceFile: File,
    val previewContent: MarkdownContent?,
    val content: MarkdownContent
)

data class NoteVM(
    val title: String,
    val createdAt: Instant?,
    val publishedAt: Instant?,
    val publishedAtValue: String,
    val path: WebPagePath,
    val content: HtmlContent,
    val previewContent: HtmlContent,
    val isTrimmed: Boolean
)
