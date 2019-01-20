package io.github.dector.glow.v2.mockimpl

import io.github.dector.glow.v2.core.DataProvider
import io.github.dector.glow.v2.core.MetaInfo
import io.github.dector.glow.v2.core.PageInfo
import java.io.File

class MockDataProvider(
        private val config: ProjectConfig,
        private val markdownParser: MarkdownParser) : DataProvider {

    private fun parseTitle(markdownFile: File): String {
        val meta = markdownParser.parseInsecureYFM(markdownFile)

        return meta["title"] ?: "n/a"
    }

    override fun fetchMetaInfo() = run {
        val pagesFolder = config.input.pagesFolder

        if (!pagesFolder.exists())
            error("Pages folder '${pagesFolder.absolutePath}' not exists.")

        var latestPageIndex = 0
        val pages = pagesFolder
                .listFiles { file ->
                    file.isFile && file.extension == "md"
                }
                .map { file ->
                    PageInfo(
                            id = (++latestPageIndex).toString(),
                            title = parseTitle(file),
                            sourceFile = file
                    )
                }

        MetaInfo(
                pages = pages
        )
    }
}