package io.github.dector.glow.v2.pipeline.notes

import io.github.dector.glow.v2.core.MarkdownContent
import io.github.dector.glow.v2.core.Note2
import io.github.dector.glow.v2.core.NoteInfo
import io.github.dector.glow.v2.implementation.MarkdownParserWrapper
import io.github.dector.glow.v2.implementation.ProjectConfig
import io.github.dector.glow.v2.implementation.markdownFileId
import io.github.dector.glow.v2.implementation.parseInstant
import java.io.File
import java.time.Instant

class DefaultNotesDataProvider(
        private val config: ProjectConfig,
        private val mdParser: MarkdownParserWrapper
) : NotesDataProvider {

    override fun fetchNotes(): List<Note2> {
        val notes = run {
            val notesFolder = config.input.notesFolder

            if (!notesFolder.exists())
                error("Notes folder '${notesFolder.absolutePath}' not exists.")
            loadNotesFrom(notesFolder)
        }

        return notes.map {
            val content = it.sourceFile.readText()
            val header = mdParser.parseYFM(content)

            Note2(
                    title = it.title,
                    sourceFile = it.sourceFile,
                    isDraft = it.isDraft,
                    content = MarkdownContent(content),
                    createdAt = header["createdAt"]?.parseInstant() ?: Instant.MIN,
                    publishedAt = header["publishedAt"]?.parseInstant() ?: Instant.MIN
            )
        }
                .sortedByDescending { it.publishedAt }
    }

    private fun loadNotesFrom(folder: File) = folder
            .listFiles { file ->
                file.isFile && file.extension == "md"
            }
            .map { file ->
                val yfm = mdParser.parseYFM(file)

                NoteInfo(
                        id = markdownFileId(file),
                        title = yfm["title"] ?: "n/a",
                        isDraft = yfm["isDraft"]?.toBoolean() ?: false,
                        createdAt = parseInstant(yfm["created"]) { Instant.MIN },
                        sourceFile = file
                )
            }

    private fun noteFile(id: String) = File(config.input.notesFolder, "$id.md")
}