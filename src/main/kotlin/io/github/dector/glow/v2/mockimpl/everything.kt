package io.github.dector.glow.v2.mockimpl

import io.github.dector.glow.v2.core.*
import java.io.File


class MockDataProvider(
        private val config: ProjectConfig,
        private val markdownParser: MarkdownParser) : DataProvider {

    private fun parseTitle(markdownFile: File): String {
        val meta = markdownParser.parseInsecureYFM(markdownFile)

        return meta["title"] ?: "n/a"
    }

    override fun fetchMetaInfo() = run {
        if (!config.pagesFolder.exists())
            error("Pages folder '${config.pagesFolder.absolutePath}' not exists.")

        var latestPageIndex = 0
        val pages = config.pagesFolder
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

class MockDataProcessor : DataProcessor {

    override fun processBlogData(blog: BlogData): ProcessedData {
        TODO()
    }
}

class MockDataPublisher : DataPublisher {

    override fun publish(data: ProcessedData): PublishResult {
        TODO()
    }
}

fun mockProjectsConfig() = ProjectConfig(
        pagesFolder = File("v2/src/pages")
)

data class ProjectConfig(
        val pagesFolder: File
)