package io.github.dector.glow.builder

import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import io.github.dector.glow.cli.GlowCommandBuildOptions
import io.github.dector.glow.logger.UiLogger
import io.github.dector.glow.logger.logger
import io.github.dector.glow.models.GlobalData
import io.github.dector.glow.models.PageModel
import io.github.dector.glow.models.ParsedPost
import io.github.dector.glow.models.PostMeta
import io.github.dector.glow.renderer.IRenderer
import io.github.dector.glow.renderer.PageType
import io.github.dector.glow.renderer.mustache.MustacheRenderer
import java.io.File
import java.io.FileFilter
import java.time.LocalDate

class GlowBuilder(
        private val opts: GlowCommandBuildOptions,
        val renderer: IRenderer = MustacheRenderer(opts.themeDir!!)) {

    private val logger = logger()

    private fun prepareDirs() {
        UiLogger.info("[Preparation] Checking output directories...")

        if (opts.clearOutputDir) {
            UiLogger.info("[Preparation] Removing existing output dir...")
            opts.outputDir?.deleteRecursively()
        }

        UiLogger.info("[Preparation] Creating output dir...")
        opts.outputDir?.mkdirs()
    }

    private fun listPostFiles() = opts.inputDir
            ?.listFiles(FileFilter { it.extension == "md" })
            ?: emptyArray()

    private fun copyAssets() {
        UiLogger.info("[Building] Copying theme assets to output...")

        File(opts.themeDir, "assets")
                .copyRecursively(File(opts.outputDir, "assets"))
    }

    private fun collectMeta(postFiles: Array<File>): List<PostMeta> {
        return postFiles
                .map(this::parsePost)
                .map { it.meta }
    }

    private fun writePage(file: File, html: String) {
        file.writeText(html)
    }

    private fun outputFileName(inputFile: File) = "${inputFile.nameWithoutExtension}.html"

    private fun outputFile(inputFile: File)
            = File(opts.outputDir, outputFileName(inputFile))

    private fun parsePost(file: File): ParsedPost {
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
                pubdate = dateTimeFromFilename(file.nameWithoutExtension),
                url = outputFileName(file),
                draft = yamlVisitor.data["draft"]?.get(0)?.toBoolean() ?: false,
                file = file)
        return ParsedPost(meta = meta, content = content)
    }

    private fun renderPost(file: File, data: GlobalData): String {
        val post = parsePost(file)
        val page = PageModel(
                title = post.meta.title,
                tags = post.meta.tags,
                pubdate = post.meta.pubdate,
                content = post.content,
                global = data)

        return render(PageType.Post, page)
    }

    private fun render(pageType: PageType, glowModel: PageModel): String
            = renderer.render(pageType, glowModel)

    private fun dateTimeFromFilename(name: String): LocalDate? {
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

    fun process(): Boolean {
        prepareDirs()

        val postFiles = listPostFiles()

        UiLogger.info("[Building] ${postFiles.size} posts found...")

        UiLogger.info("[Building] Posts list...")
        val globalData = GlobalData(
                blogName = opts.blogTitle,
                posts = collectMeta(postFiles))

        UiLogger.info("[Building] Posts...")
        val filteredGlobal = globalData
                .copy(posts = globalData.posts.filter { !it.draft })
        filteredGlobal.posts
                .map { it.file }
                .forEach { writePage(outputFile(it), renderPost(it, filteredGlobal)) }

        writeArchivePage(filteredGlobal)
        writeIndexPage(filteredGlobal)

        copyAssets()

        UiLogger.info("[Building] ${globalData.posts.size} file(s) proceed...")

        return true
    }

    private fun writeArchivePage(data: GlobalData) {
        UiLogger.info("[Building] Archive page...")

        val archiveFile = File(opts.outputDir, "archive.html")
        val page = PageModel(
                global = data,
                title = "Archive",
                content = "",
                pubdate = null)
        writePage(archiveFile, render(PageType.Archive, page))
    }

    private fun writeIndexPage(data: GlobalData) {
        UiLogger.info("[Building] Index page...")

        val archiveFile = File(opts.outputDir, "index.html")
        val page = PageModel(
                global = data,
                title = "",
                content = "",
                pubdate = null)
        writePage(archiveFile, render(PageType.Index, page))
    }
}