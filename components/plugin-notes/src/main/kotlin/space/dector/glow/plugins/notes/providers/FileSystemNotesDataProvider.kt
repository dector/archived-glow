package space.dector.glow.plugins.notes.providers

import space.dector.glow.engine.MarkdownContent
import space.dector.glow.plugins.notes.Note
import space.dector.glow.plugins.notes.NotesDataProvider
import space.dector.glow.plugins.notes.parseMarkdownPartsFrom
import space.dector.glow.plugins.notes.providers.MetaProperty.CreatedAt
import space.dector.glow.plugins.notes.providers.MetaProperty.Draft
import space.dector.glow.plugins.notes.providers.MetaProperty.IsMicro
import space.dector.glow.plugins.notes.providers.MetaProperty.PublishedAt
import space.dector.glow.plugins.notes.providers.MetaProperty.Tags
import space.dector.glow.plugins.notes.providers.MetaProperty.Title
import space.dector.glow.plugins.notes.providers.MetaProperty.UpdatedAt
import space.dector.ktx.joinAsText
import java.io.File
import java.time.Instant
import kotlin.reflect.KClass

class FileSystemNotesDataProvider(
    private val notesDir: File,
    private val sortingComparator: Comparator<Note> = DefaultSortingComparator
) : NotesDataProvider {

    override fun fetchNotes(): List<Note> =
        loadMarkdownFiles(notesDir)
            //.filterNot { it.get<Draft>()?.value ?: false }
            .map(MarkdownFile::toNote)
            .sortedWith(sortingComparator)

    private companion object {

        val DefaultSortingComparator: Comparator<Note> =
            compareByDescending { it.publishedAt ?: Instant.MIN }
    }
}

private fun loadMarkdownFiles(dir: File): List<MarkdownFile> {
    require(dir.exists()) { "Notes folder '${dir.absolutePath}' not exists." }

    return dir.listFiles()!!
        .filter { it.extension == "md" }
        .filter(File::isFile)
        .map(MarkdownFile.Companion::parseFrom)
}

private fun MarkdownFile.toNote(): Note = run {
    val previewContent = buildPreviewContent()

    Note(
        sourceFile = sourceFile,

        title = get<Title>()?.value ?: "",
        isDraft = get<Draft>()?.value ?: false,
        isMicro = get<IsMicro>()?.value ?: false,
        createdAt = get<CreatedAt>()?.value,
        publishedAt = get<PublishedAt>()?.value,
        updatedAt = get<UpdatedAt>()?.value,

        tags = get<Tags>()?.value?.map(String::trim) ?: emptyList(),

        content = content,
        previewContent = previewContent
    )
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
    data class IsMicro(val value: Boolean) : MetaProperty()
    data class CreatedAt(val value: Instant) : MetaProperty()
    data class UpdatedAt(val value: Instant) : MetaProperty()
    data class PublishedAt(val value: Instant) : MetaProperty()
    data class Tags(val value: List<String>) : MetaProperty()

    data class Undefined(val key: String, val value: String) : MetaProperty()
}

private inline fun <reified T : MetaProperty> MarkdownFile.get(): T? =
    get(T::class)

@Suppress("UNCHECKED_CAST")
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
                "isMicro" -> IsMicro(value.toBoolean())
                "createdAt" -> parseCreatedAt(value)?.let(::CreatedAt)
                "updatedAt" -> parseUpdatedAt(value)?.let(::UpdatedAt)
                "publishedAt" -> parsePublishedAt(value)?.let(::PublishedAt)
                "tags" -> Tags(value.split(",").map(String::trim))
                else -> MetaProperty.Undefined(key, value)
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
