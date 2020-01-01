package io.github.dector.glow.plugins.notes

import io.github.dector.glow.core.MarkdownContent
import io.github.dector.glow.core.config.Config
import io.github.dector.glow.core.parser.parseCreatedAt
import io.github.dector.glow.core.parser.parsePublishedAt
import io.github.dector.glow.core.parser.parseUpdatedAt
import io.github.dector.glow.joinAsText
import io.github.dector.glow.plugins.notes.MetaProperty.CreatedAt
import io.github.dector.glow.plugins.notes.MetaProperty.Draft
import io.github.dector.glow.plugins.notes.MetaProperty.PublishedAt
import io.github.dector.glow.plugins.notes.MetaProperty.Tags
import io.github.dector.glow.plugins.notes.MetaProperty.Title
import io.github.dector.glow.plugins.notes.MetaProperty.UpdatedAt
import java.io.File
import java.time.Instant
import kotlin.reflect.KClass

class DefaultNotesDataProvider(
    private val config: Config
) : NotesDataProvider {

    override fun fetchNotes(): List<Note2> =
        loadMarkdownFiles(config.plugins.notes.sourceDir)
            .filterNot { it.get<Draft>()?.value ?: false }
            .map { file ->
                val previewContent = file.buildPreviewContent()

                Note2(
                    sourceFile = file.sourceFile,

                    title = file.get<Title>()?.value ?: "",
                    isDraft = file.get<Draft>()?.value ?: false,
                    createdAt = file.get<CreatedAt>()?.value,
                    publishedAt = file.get<PublishedAt>()?.value,

                    content = file.content,
                    previewContent = previewContent
                )
            }.sortedByDescending { it.publishedAt ?: Instant.MIN }
}

private fun loadMarkdownFiles(dir: File): List<MarkdownFile> {
    require(dir.exists()) { "Notes folder '${dir.absolutePath}' not exists." }

    return dir.listFiles()!!
        .filter { it.extension == "md" }
        .filter(File::isFile)
        .map(MarkdownFile.Companion::parseFrom)
}

private data class MarkdownFile(
    val sourceFile: File,
    val meta: Set<MetaProperty>,
    val content: MarkdownContent
) {
    companion object
}

private sealed class MetaProperty {
    data class Title(val value: String) : MetaProperty()
    data class Draft(val value: Boolean) : MetaProperty()
    data class CreatedAt(val value: Instant) : MetaProperty()
    data class UpdatedAt(val value: Instant) : MetaProperty()
    data class PublishedAt(val value: Instant) : MetaProperty()
    data class Tags(val value: List<String>) : MetaProperty()
}

private inline fun <reified T : MetaProperty> MarkdownFile.get(): T? =
    get(T::class)

private operator fun <T : MetaProperty> MarkdownFile.get(klass: KClass<T>): T? {
    return meta.find { klass.isInstance(it) } as T?
}

private fun MarkdownFile.Companion.parseFrom(file: File): MarkdownFile {
    val parseResult = parseMarkdownPartsFrom(file.readText())

    return MarkdownFile(
        sourceFile = file,
        meta = parseMeta(parseResult.header),
        content = MarkdownContent(parseResult.content)
    )
}

private fun parseMeta(header: String?): Set<MetaProperty> {
    header ?: return emptySet()

    return header.lines()
        .map { line ->
            line.substringBefore(':').trim() to line.substringAfter(':').trim()
        }
        .mapNotNull { (key, value) ->
            when (key) {
                "title" -> Title(value)
                "draft", "isDraft" -> Draft(value.toBoolean())
                "createdAt" -> parseCreatedAt(value)?.let(::CreatedAt)
                "updatedAt" -> parseUpdatedAt(value)?.let(::UpdatedAt)
                "publishedAt" -> parsePublishedAt(value)?.let(::PublishedAt)
                "tags" -> Tags(value.split(",").map(String::trim))
                else -> null
            }
        }
        .toSet()
}

private fun MarkdownFile.buildPreviewContent(): MarkdownContent {
    val lines = content.value.trim().lines()

    fun String.isCutMarker() = contains("__cut") &&
        trim().let { it.startsWith("<!--") && it.endsWith("-->") }

    val preview = lines.takeWhile { !it.isCutMarker() }
    if (preview != lines) return MarkdownContent(preview.joinAsText())

    // If cut marker isn't present - take first paragraph

    return lines.takeWhile { it.isNotBlank() }
        .joinAsText()
        .let(::MarkdownContent)
}
