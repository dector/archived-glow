package io.github.dector.legacy.glow.builder

import io.github.dector.glow.core.logger.UILogger
import io.github.dector.legacy.glow.builder.models.BlogData
import io.github.dector.legacy.glow.builder.models.PageData
import io.github.dector.legacy.glow.builder.models.PostMeta
import io.github.dector.legacy.glow.builder.parser.DefaultPostParser
import io.github.dector.legacy.glow.builder.parser.IPostParser
import io.github.dector.legacy.glow.builder.parser.ParsedPost
import io.github.dector.legacy.glow.builder.renderer.IRenderer
import io.github.dector.legacy.glow.builder.renderer.PageType
import io.github.dector.legacy.glow.builder.renderer.mustache.MustacheRenderer
import io.github.dector.legacy.glow.cli.GlowCommandBuildOptions
import io.github.dector.legacy.glow.utils.nextOrNull
import io.github.dector.legacy.glow.utils.prevOrNull
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

        UILogger.info("[Building] ${blogData.posts.size} posts found...")

        buildPostPages(blogData)
        buildArchivePage(blogData)
        buildIndexPage(blogData)

        copyAssets()

        UILogger.info("[Building] ${blogData.posts.size} file(s) proceed...")

        return true
    }

    // --- Preparations

    private fun prepareDirs() {
        UILogger.info("[Preparation] Checking output directories...")

        if (opts.clearOutputDir) {
            UILogger.info("[Preparation] Removing existing output dir...")
            opts.outputDir?.deleteRecursively()
        }

        UILogger.info("[Preparation] Creating output dir...")
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
        UILogger.info("[Building] Posts...")

        for (item in blogData.posts) {
            // Newer posts are in the beginning
            val after = blogData.posts.prevOrNull(item)
            val before = blogData.posts.nextOrNull(item)

            val inputFile = item.file
            val outputFile = outputPostFile(inputFile)
            val post = postParser.parse(inputFile)
            val content = renderPost(post, blogData, Pair(before, after))

            outputFile.writeText(content)
        }
    }

    // --- Archive Page

    private fun buildArchivePage(blogData: BlogData) {
        UILogger.info("[Building] Archive page...")

        val page = PageData(
                blog = blogData,
                title = "Archive")
        val content = renderPage(PageType.Archive, page)

        outputDirFile("archive.html")
                .writeText(content)
    }

    // --- Index Page

    private fun buildIndexPage(data: BlogData) {
        UILogger.info("[Building] Index page...")

        val page = PageData(
                blog = data)
        val content = renderPage(PageType.Index, page)

        outputDirFile("index.html")
                .writeText(content)
    }

    // --- Assets

    private fun copyAssets() {
        UILogger.info("[Building] Copying theme assets to output...")

        themeDirFile("assets")
                .copyRecursively(outputDirFile("assets"))
    }

    // --- Rendering

    private fun renderPost(post: ParsedPost, data: BlogData, prevAndNext: Pair<PostMeta?, PostMeta?> = Pair(null, null)): String {
        val page = PageData(
                title = post.meta.title,
                tags = post.meta.tags,
                pubDate = post.meta.pubDate,
                content = post.content,
                prev = prevAndNext.first,
                next = prevAndNext.second,
                blog = data)

        return renderPage(PageType.Post, page)
    }

    private fun renderPage(pageType: PageType, page: PageData): String = renderer.render(pageType, page)

    // --- File tools

    private fun outputPostFile(inputFile: File): File = outputDirFile(outputPostFileName(inputFile))

    private fun outputPostFileName(inputFile: File): String = urlBuilder(inputFile.nameWithoutExtension)

    private fun outputDirFile(name: String): File = File(opts.outputDir, name)

    private fun themeDirFile(name: String): File = File(opts.themeDir, name)

    // ---

}

fun defaultRenderer(opts: GlowCommandBuildOptions): IRenderer = MustacheRenderer(templatesDir = opts.themeDir!!)