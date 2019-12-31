package io.github.dector.glow.plugins.notes

import io.github.dector.glow.core.MarkdownContent
import io.github.dector.glow.core.config.Config
import io.github.dector.glow.core.parser.MarkdownParser
import io.github.dector.glow.core.parser.markdownFileId
import io.github.dector.glow.core.parser.parseCreatedAt
import io.github.dector.glow.core.parser.parsePublishedAt
import java.io.File
import java.time.Instant

class DefaultNotesDataProvider(
    private val config: Config,
    private val mdParser: MarkdownParser<*>
) : NotesDataProvider {

    override fun fetchNotes(): List<Note2> {
        val files = loadMarkdownFiles(config.plugins.notes.sourceDir)
        // TODO use it

        val notes = run {
            val notesFolder = config.plugins.notes.sourceDir

            if (!notesFolder.exists())
                error("Notes folder '${notesFolder.absolutePath}' not exists.")
            loadNotesFrom(notesFolder)
        }

        return notes.map { note ->
            val content = note.sourceFile.readText()
            val header = mdParser.parseInsecureYFM(content)

            val previewContent = run {
                val lines = content.lines()
                val cutLineIndex = lines
                    .indexOfFirst { line ->
                        line.contains("__cut")
                            && line.trim().startsWith("<!--")
                            && line.trim().endsWith("-->")
                    }

                if (cutLineIndex != -1)
                    lines.take(cutLineIndex + 1).joinToString(separator = "\n")
                else null
            }

            Note2(
                title = note.title,
                sourceFile = note.sourceFile,
                isDraft = note.isDraft,
                previewContent = previewContent?.let { MarkdownContent(it) },
                content = MarkdownContent(content),
                createdAt = parseCreatedAt(header["createdAt"]),
                publishedAt = parsePublishedAt(header["publishedAt"])
            )
        }.sortedByDescending { it.publishedAt ?: Instant.MIN }
    }

    private fun loadNotesFrom(folder: File) = folder
        .listFiles { file ->
            file.isFile && file.extension == "md"
        }!!
        .map { file ->
            val yfm = mdParser.parseInsecureYFM(file)

            NoteInfo(
                id = markdownFileId(file),
                title = yfm["title"] ?: "",
                isDraft = yfm["isDraft"]?.toBoolean() ?: false,
                createdAt = parseCreatedAt(yfm["created"]) ?: Instant.MIN,
                sourceFile = file
            )
        }
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
    return emptySet() // TODO parse it
}
