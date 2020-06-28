package io.github.dector.glow.plugins.notes

import io.github.dector.glow.coordinates.Coordinates
import io.github.dector.glow.engine.HtmlContent
import io.github.dector.glow.engine.MarkdownContent
import java.io.File
import java.time.Instant

data class Note(
    val title: String,
    val createdAt: Instant?,
    val publishedAt: Instant?,
    val updatedAt: Instant?,
    val isDraft: Boolean,
    val tags: List<String>,
    val sourceFile: File,
    val previewContent: MarkdownContent?,
    val content: MarkdownContent
)

data class NoteVM(
    val rawModel: Note,

    val title: String,
    val coordinates: Coordinates.Endpoint,

    val publishedAndUpdatedStr: String,
//    val publishedAtStr: String,
//    val updatedAtStr: String?,

    val content: HtmlContent,
    val previewContent: HtmlContent,
    val isTrimmed: Boolean
)
