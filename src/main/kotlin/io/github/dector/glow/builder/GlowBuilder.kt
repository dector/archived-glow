package io.github.dector.glow.builder

import io.github.dector.glow.builder.models.BlogData
import io.github.dector.glow.builder.models.PageData
import io.github.dector.glow.builder.models.PostMeta
import io.github.dector.glow.builder.parser.DefaultPostParser
import io.github.dector.glow.builder.parser.IPostParser
import io.github.dector.glow.builder.parser.ParsedPost
import io.github.dector.glow.builder.renderer.IRenderer
import io.github.dector.glow.builder.renderer.PageType
import io.github.dector.glow.builder.renderer.mustache.MustacheRenderer
import io.github.dector.glow.cli.GlowCommandBuildOptions
import io.github.dector.glow.logger.UiLogger
import java.io.File
import java.io.FileFilter

class GlowBuilder(
        private val opts: GlowCommandBuildOptions,
        private val urlBuilder: (String) -> String = { filename -> "$filename.html" },
        private val postParser: IPostParser = DefaultPostParser(urlBuilder),
        private val renderer: IRenderer = defaultRenderer(opts)) {

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
        }.sortedByDescending { it.pubDate }

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
                .map { Pair(outputPostFile(it), postParser.parse(it)) }
                .map { (file, post) -> Pair(file, renderPost(post, blogData)) }
                .forEach { (outputFile, content) -> outputFile.writeText(content) }
    }

    // --- Archive Page

    private fun buildArchivePage(blogData: BlogData) {
        UiLogger.info("[Building] Archive page...")

        val page = PageData(
                blog    = blogData,
                title   = "Archive")
        val content = renderPage(PageType.Archive, page)

        outputDirFile("archive.html")
                .writeText(content)
    }

    // --- Index Page

    private fun buildIndexPage(data: BlogData) {
        UiLogger.info("[Building] Index page...")

        val page = PageData(
                blog    = data)
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

    private fun renderPost(post: ParsedPost, data: BlogData): String {
        val page = PageData(
                title   = post.meta.title,
                tags    = post.meta.tags,
                pubDate = post.meta.pubDate,
                content = post.content,
                blog    = data)

        return renderPage(PageType.Post, page)
    }

    private fun renderPage(pageType: PageType, page: PageData): String
            = renderer.render(pageType, page)

    // --- File tools

    private fun outputPostFile(inputFile: File): File
            = outputDirFile(outputPostFileName(inputFile))

    private fun outputPostFileName(inputFile: File): String
            = urlBuilder(inputFile.nameWithoutExtension)

    private fun outputDirFile(name: String): File
            = File(opts.outputDir, name)

    private fun themeDirFile(name: String): File
            = File(opts.themeDir, name)

    // ---

}

fun defaultRenderer(opts: GlowCommandBuildOptions): IRenderer
        = MustacheRenderer(templatesDir = opts.themeDir!!)