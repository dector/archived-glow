package space.dector.glow.plugins.notes

import space.dector.glow.coordinates.Coordinates
import space.dector.glow.engine.HtmlContent
import space.dector.glow.engine.MarkdownContent
import java.io.File
import java.time.Instant

data class Note(
    val title: String,
    val createdAt: Instant?,
    val publishedAt: Instant?,
    val updatedAt: Instant?,
    val isDraft: Boolean,
    val isMicro: Boolean,
    val tags: List<String>,
    val sourceFile: File,
    val previewContent: MarkdownContent?,
    val content: MarkdownContent
)

data class NoteVM(
    val rawModel: Note,

    val title: String,
    val url: String,

    val coordinates: Coordinates.Endpoint,

    val createdText: String,
    val updatedText: String,
    val publishedAndUpdatedStr: String,
//    val publishedAtStr: String,
//    val updatedAtStr: String?,

    val content: HtmlContent,
    val previewContent: HtmlContent,
    val isTrimmed: Boolean,

    val tags: List<TagVM>
)

data class TagVM(
    val name: String,
    val url: String
)
