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

    override fun render(page: Page): RenderedPage {
        return RenderedPage(
                path = PagePath("${page.info.id}-${page.info.title}"),
                content = "Page with title '${page.info.title}'"
        )
    }

    override fun processBlogData(blog: BlogData) = error("")
}

class MockDataPublisher(
        private val config: ProjectConfig) : DataPublisher {

    override fun publishPage(page: RenderedPage) {
        val file = File(config.output.pagesFolder, pageFileName(page))
        file.parentFile.mkdirs()

        if (file.exists()) {
            println("File '${file.absolutePath}' exists. Skipping.")
        } else {
            file.writeText(page.content)
        }

    }

    private fun pageFileName(page: RenderedPage) = "${page.path.path}.html"

    override fun publish(data: ProcessedData) = error("")
}

fun mockProjectsConfig() = ProjectConfig(
        pagesFolder = File("v2/src/pages"),
        output = OutputConfig(
                pagesFolder = File("v2/out2/pages")
        )
)

data class ProjectConfig(
        val pagesFolder: File,  // TODO move to InputConfig
        val output: OutputConfig
)

data class OutputConfig(
        val pagesFolder: File
)

data class PagePath(
        val path: String
)