package io.github.dector.glow

import com.beust.jcommander.JCommander
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import io.github.dector.glow.renderer.IRenderer
import io.github.dector.glow.renderer.PageType
import io.github.dector.glow.renderer.mustache.MustacheRenderer
import io.github.dector.glow.tools.StopWatch
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileFilter
import java.time.LocalDate

val CliHeader = """
      _  |  _
     (_| | (_) \/\/
      _|            v ${BuildConfig.VERSION}
"""

fun main(args: Array<String>) {
    val stopWatch = StopWatch().start()

    val logger = LoggerFactory.getLogger("")
    logger.info(CliHeader)

    val opts = parseArguments(*args)

    if (!OptionsValidator().validate(opts))
        return

    when (opts.command) {
        GlowCommandInitOptions.Value -> {
            GlowProjectCreator(opts.commandInitOptions).process()
        }
        GlowCommandBuildOptions.Value -> Glow(opts.commandBuildOptions).process()
        else -> logger.error("Command ${opts.command} not defined.")
    }

    logger.info("Finished in ${stopWatch.stop().timeFormatted()}.")
}

internal fun parseArguments(vararg args: String): GlowOptions {
    val commandMain = GlowCommandMainOptions()
    val jc = JCommander(commandMain)

    val commandNew = GlowCommandInitOptions().also { jc.addCommand(GlowCommandInitOptions.Value, it) }
    val commandBuild = GlowCommandBuildOptions().also { jc.addCommand(GlowCommandBuildOptions.Value, it) }

    jc.parse(*args)

    return GlowOptions(
            command = jc.parsedCommand,
            commandMainOptions = commandMain,
            commandInitOptions = commandNew,
            commandBuildOptions = commandBuild)
}

class Glow(private val opts: GlowCommandBuildOptions,
           val renderer: IRenderer = MustacheRenderer(opts.themeDir!!)) {

    private val logger = LoggerFactory.getLogger(javaClass)

    private fun prepareDirs() {
        logger.info("Preparing output directories.")

        if (opts.clearOutputDir) {
            logger.info("Removing existing output dir.")
            opts.outputDir?.deleteRecursively()
        }

        logger.info("Creating output dir.")
        opts.outputDir?.mkdirs()
    }

    private fun listPostFiles() = File(opts.inputDir, "posts/")
            .listFiles(FileFilter { it.extension == "md" })

    private fun copyAssets() {
        logger.info("Copying theme assets to output.")

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

    fun process() {
        prepareDirs()

        val postFiles = listPostFiles()

        logger.info("${postFiles.size} posts found.")

        logger.info("Building posts list.")
        val globalData = GlobalData(
                blogName = opts.blogTitle,
                posts = collectMeta(postFiles))

        logger.info("Building posts.")
        val filteredGlobal = globalData
                .copy(posts = globalData.posts.filter { !it.draft })
        filteredGlobal.posts
                .map { it.file }
                .forEach { writePage(outputFile(it), renderPost(it, filteredGlobal)) }

        writeArchivePage(filteredGlobal)
        writeIndexPage(filteredGlobal)

        copyAssets()

        logger.info("Done. ${globalData.posts.size} file(s) proceed.")
    }

    private fun writeArchivePage(data: GlobalData) {
        logger.info("Building archive page.")

        val archiveFile = File(opts.outputDir, "archive.html")
        val page = PageModel(
                global = data,
                title = "Archive",
                content = "",
                pubdate = null)
        writePage(archiveFile, render(PageType.Archive, page))
    }

    private fun writeIndexPage(data: GlobalData) {
        logger.info("Building index page.")

        val archiveFile = File(opts.outputDir, "index.html")
        val page = PageModel(
                global = data,
                title = "",
                content = "",
                pubdate = null)
        writePage(archiveFile, render(PageType.Index, page))
    }
}