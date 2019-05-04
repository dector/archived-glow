package io.github.dector.glow.v2.implementation

import io.github.dector.glow.v2.core.*
import io.github.dector.glow.v2.core.components.DataProvider
import java.io.File
import java.time.Instant

class DefaultDataProvider(
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
                            content = MarkdownContent(it.sourceFile.readText()),
                            isSection = it.isSection
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
            val content = it.sourceFile.readText()
            val header = markdownParser.parseInsecureYFM(content)

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

    private fun parseMetaInfo(markdownFile: File): Map<String, String> {
        return parseYFM(markdownFile)
    }

    private fun parseYFM(markdownFile: File) =
            markdownParser.parseInsecureYFM(markdownFile)

    private fun File.listFilesRecursively(): List<File> = listFiles()
            .flatMap {
                if (it.isDirectory) it.listFilesRecursively()
                else listOf(it)
            }

    private fun loadPagesFrom(folder: File) = folder
            .listFilesRecursively()
            .filter { file ->
                file.isFile && file.extension == "md"
            }
            .map { file ->
                val meta = parseMetaInfo(file)

                PageInfo(
                        id = markdownFileId(file),
                        title = meta["title"] ?: "n/a",
                        isSection = meta["isSection"]?.toBoolean() ?: false,
                        sourceFile = file
                )
            }

    private fun String.parseInstant() = try {
        Instant.parse(this)
    } catch (e: Throwable) {
        null
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
                        isDraft = yfm["isDraft"]?.toBoolean() ?: false,
                        createdAt = parseInstant(yfm["created"]) { Instant.MIN },
                        sourceFile = file
                )
            }

    private fun markdownFileId(file: File) = file.nameWithoutExtension
            .toLowerCase()
            .replace(" ", "-")

    private fun noteFile(id: String) = File(config.input.notesFolder, "$id.md")
}