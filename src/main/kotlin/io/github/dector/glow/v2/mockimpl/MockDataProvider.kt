package io.github.dector.glow.v2.mockimpl

import io.github.dector.glow.v2.core.*
import java.io.File
import java.time.Instant

class MockDataProvider(
        private val config: ProjectConfig,
        private val markdownParser: MarkdownParser<*>) : DataProvider {

    override fun fetchPages(): List<Page2> {
        val pagesFolder = config.input.pagesFolder

        if (!pagesFolder.exists())
            error("Pages folder '${pagesFolder.absolutePath}' not exists.")

        return loadPagesFrom(pagesFolder)
                .map {
                    Page2(
                            title = it.title,
                            createdAt = null,
                            sourceFile = it.sourceFile,
                            content = MarkdownContent(it.sourceFile.readText())
                    )
                }
    }

    override fun fetchNotes(): List<Note2> {
        val notes = run {
            val notesFolder = config.input.notesFolder

            if (!notesFolder.exists())
                error("Notes folder '${notesFolder.absolutePath}' not exists.")
            loadNotesFrom(notesFolder)
        }

        return notes.map {
            Note2(
                    title = it.title,
                    sourceFile = it.sourceFile,
                    isDraft = it.isDraft,
                    content = MarkdownContent(it.sourceFile.readText()),
                    createdAt = null
            )
        }
    }

    private fun parseTitle(markdownFile: File): String {
        val meta = parseYFM(markdownFile)

        return meta["title"] ?: "n/a"
    }

    private fun parseYFM(markdownFile: File) =
            markdownParser.parseInsecureYFM(markdownFile)

    private fun loadPagesFrom(folder: File) = folder
            .listFiles { file ->
                file.isFile && file.extension == "md"
            }
            .map { file ->
                PageInfo(
                        id = markdownFileId(file),
                        title = parseTitle(file),
                        sourceFile = file
                )
            }

    private fun parseInstant(str: String?, fallback: () -> Instant): Instant {
        str ?: fallback()

        return try {
            Instant.parse(str)
        } catch (e: Throwable) {
            fallback()
        }
    }

    private fun loadNotesFrom(folder: File) = folder
            .listFiles { file ->
                file.isFile && file.extension == "md"
            }
            .map { file ->
                val yfm = parseYFM(file)

                NoteInfo(
                        id = markdownFileId(file),
                        title = yfm["title"] ?: "n/a",
                        isDraft = yfm["draft"]?.toBoolean() ?: false,
                        createdAt = parseInstant(yfm["created"]) { Instant.MIN },
                        sourceFile = file
                )
            }

    private fun markdownFileId(file: File) = file.nameWithoutExtension
            .toLowerCase()
            .replace(" ", "-")

    private fun noteFile(id: String) = File(config.input.notesFolder, "$id.md")
}