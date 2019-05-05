package io.github.dector.glow.v2.pipeline.pages

import io.github.dector.glow.v2.core.MarkdownContent
import io.github.dector.glow.v2.core.Page2
import io.github.dector.glow.v2.core.PageInfo
import io.github.dector.glow.v2.implementation.MarkdownParserWrapper
import io.github.dector.glow.v2.implementation.ProjectConfig
import io.github.dector.glow.v2.implementation.markdownFileId
import java.io.File

class DefaultPagesDataProvider(
        private val config: ProjectConfig,
        private val mdParser: MarkdownParserWrapper) : PagesDataProvider {

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
                val meta = mdParser.parseMetaInfo(file)

                PageInfo(
                        id = markdownFileId(file),
                        title = meta["title"] ?: "n/a",
                        isSection = meta["isSection"]?.toBoolean() ?: false,
                        sourceFile = file
                )
            }
}