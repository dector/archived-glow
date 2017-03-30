package io.github.dector.glow.builder

import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import io.github.dector.glow.builder.models.BlogData
import io.github.dector.glow.builder.models.PageModel
import io.github.dector.glow.builder.models.ParsedPost
import io.github.dector.glow.builder.models.PostMeta
import io.github.dector.glow.builder.renderer.IRenderer
import io.github.dector.glow.builder.renderer.PageType
import io.github.dector.glow.builder.renderer.mustache.MustacheRenderer
import io.github.dector.glow.cli.GlowCommandBuildOptions
import io.github.dector.glow.logger.UiLogger
import java.io.File
import java.io.FileFilter
import java.time.LocalDate

class GlowBuilder(
        private val opts: GlowCommandBuildOptions,
        val renderer: IRenderer = MustacheRenderer(opts.themeDir!!)) {

    private val postParser: IPostParser = DefaultPostParser(
            { file -> outputPostFileName(file) } )

    fun process(): Boolean {
        prepareDirs()

        val blogData = collectBlogData(false)

        UiLogger.info("[Building] ${blogData.posts.size} posts found...")

        buildPostPages(blogData)
        buildArchivePage(blogData)
        buildIndexPage(blogData)

        copyAssets()

        UiLogger.info("[Building] ${blogData.posts.size} file(s) proceed...")

        return true
    }

    // --- Preparations

    private fun prepareDirs() {
        UiLogger.info("[Preparation] Checking output directories...")

        if (opts.clearOutputDir) {
            UiLogger.info("[Preparation] Removing existing output dir...")
            opts.outputDir?.deleteRecursively()
        }

        UiLogger.info("[Preparation] Creating output dir...")
        opts.outputDir?.mkdirs()
    }

    // --- Posts and Blog Meta

    private fun collectBlogData(includeDrafts: Boolean = false): BlogData {
        val posts = collectBlogPostsMeta().let {
            if (includeDrafts) it
            else it.filter { !it.isDraft }
        }

        return BlogData(
                title = opts.blogTitle,
                posts = posts)
    }

    private fun collectBlogPostsMeta(): List<PostMeta> {
        return listPostFiles()
                .map(postParser::parse)
                .map { it.meta }
    }

    private fun listPostFiles(): Array<File> {
        return opts.inputDir
            ?.listFiles(FileFilter { it.extension == "md" })
            ?: emptyArray()
    }

    // --- Post Pages

    private fun buildPostPages(blogData: BlogData) {
        UiLogger.info("[Building] Posts...")

        blogData.posts
                .map(PostMeta::file)
                .map { Pair(outputPostFile(it), renderPost(it, blogData)) }
                .forEach { (outputFile, content) -> outputFile.writeText(content) }
    }

    // --- Archive Page

    private fun buildArchivePage(blogData: BlogData) {
        UiLogger.info("[Building] Archive page...")

        val page = PageModel(
                blog = blogData,
                title = "Archive",
                content = "",
                pubdate = null)
        val content = renderPage(PageType.Archive, page)

        outputDirFile("archive.html")
                .writeText(content)
    }

    // --- Index Page

    private fun buildIndexPage(data: BlogData) {
        UiLogger.info("[Building] Index page...")

        val page = PageModel(
                blog = data,
                title = "",
                content = "",
                pubdate = null)
        val content = renderPage(PageType.Index, page)

        outputDirFile("index.html")
                .writeText(content)
    }

    // --- Assets

    private fun copyAssets() {
        UiLogger.info("[Building] Copying theme assets to output...")

        themeDirFile("assets")
                .copyRecursively(outputDirFile("assets"))
    }

    // --- Rendering

    private fun renderPost(file: File, data: BlogData): String {
        val post = postParser.parse(file)
        val page = PageModel(
                title = post.meta.title,
                tags = post.meta.tags,
                pubdate = post.meta.pubdate,
                content = post.content,
                blog = data)

        return renderPage(PageType.Post, page)
    }

    private fun renderPage(pageType: PageType, glowModel: PageModel): String
            = renderer.render(pageType, glowModel)

    // --- File tools

    private fun outputPostFile(inputFile: File): File
            = outputDirFile(outputPostFileName(inputFile))

    private fun outputPostFileName(inputFile: File): String
            = "${inputFile.nameWithoutExtension}.html"

    private fun outputDirFile(name: String): File
            = File(opts.outputDir, name)

    private fun themeDirFile(name: String): File
            = File(opts.themeDir, name)

    // ---

}

interface IPostParser {

    fun parse(file: File): ParsedPost
}

class DefaultPostParser(
        private val postUrlBuilder: (File) -> String) : IPostParser {

    override fun parse(file: File): ParsedPost {
        val parserOptions = MutableDataSet().apply {
            set(Parser.EXTENSIONS, listOf(YamlFrontMatterExtension.create()))
        }

        val parser = Parser.builder(parserOptions).build()
        val renderer = HtmlRenderer.builder().build()

        val html = file.readText()

        val doc = parser.parse(html)
        val content = renderer.render(doc).trim()

        val yamlVisitor = AbstractYamlFrontMatterVisitor()
        yamlVisitor.visit(doc)

        val meta = PostMeta(
                title = yamlVisitor.data["title"]?.get(0) ?: "",
                tags = yamlVisitor.data["tags"]?.get(0)?.split(",")?.map(String::trim) ?: emptyList(),
                pubdate = postPubDateFromFilename(file.nameWithoutExtension),
                url = postUrlBuilder(file),
                isDraft = yamlVisitor.data["draft"]?.get(0)?.toBoolean() ?: false,
                file = file)
        return ParsedPost(meta = meta, content = content)
    }

    private fun postPubDateFromFilename(name: String): LocalDate? {
        val parts = name.split("-")

        if (parts.size < 3)
            return null

        val year = parts[0].toIntOrNull()
        val month = parts[1].toIntOrNull()
        val day = parts[2].toIntOrNull()

        if (year == null || month == null || day == null)
            return null

        return LocalDate.of(year, month, day)
    }
}