package io.github.dector.glow

import com.beust.jcommander.JCommander
import com.samskivert.mustache.Mustache
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import io.github.dector.glow.tools.StopWatch
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileFilter
import java.io.Reader
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

val version = "0.1"

fun cliHeader(): String = """
    >
    >      _  |  _
    >     (_| | (_) \/\/
    >      _|            v $version
    >""".trimMargin(">")

fun main(args: Array<String>) {
    val stopWatch = StopWatch().start()

    val logger = LoggerFactory.getLogger("")
    logger.info(cliHeader())

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
           val renderer: IRenderer = JMustacheRenderer(opts.themeDir!!)) {

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
                pubdate = dateTimeFromFilename(file.nameWithoutExtension),
                url = outputFileName(file),
                draft = yamlVisitor.data["draft"]?.get(0)?.toBoolean() ?: false,
                file = file)
        return ParsedPost(meta = meta, content = content)
    }

    private fun buildPage(file: File, data: GlobalData): String {
        val post = parsePost(file)
        val glowModel = PageModel(
                title = post.meta.title,
                pubdate = post.meta.pubdate,
                content = post.content,
                global = data)

        return renderer.render(PageType.Post, glowModel)
    }

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
                .forEach { writePage(outputFile(it), buildPage(it, filteredGlobal)) }

        copyAssets()

        logger.info("Done. ${globalData.posts.size} file(s) proceed.")
    }
}

enum class PageType {
    Post
}

interface IRenderFormatter {

    fun formatPubDate(date: LocalDate?): String
    fun formatPubDateHint(date: LocalDate?): String
}

class DefaultRenderFormatter : IRenderFormatter {

    override fun formatPubDate(date: LocalDate?): String {
        date ?: return ""

        return DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
                .format(date)
    }

    override fun formatPubDateHint(date: LocalDate?): String {
        date ?: return ""

        return DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale.ENGLISH)
                .format(date)
    }
}

interface IRenderer {

    fun render(pageType: PageType, model: PageModel): String
}

class JMustacheRenderer(
        private val templatesDir: File,
        val formatter: IRenderFormatter = DefaultRenderFormatter()) : IRenderer {

    val mustache: Mustache.Compiler = Mustache.compiler()
            .withLoader { name -> templateFile(name).reader() }
            .escapeHTML(false)

    override fun render(pageType: PageType, model: PageModel): String = mustache
            .compile(templateReader(pageType))
            .execute(buildContext(model))

    private fun buildContext(pageModel: PageModel) = mapOf(
            "blogTitle" to pageModel.global.blogName,
            "blogPosts" to pageModel.global.posts,
            "title" to pageModel.title,
            "pubdate" to formatter.formatPubDate(pageModel.pubdate),
            "pubdateHint" to formatter.formatPubDateHint(pageModel.pubdate),
            "content" to pageModel.content)

    private fun templateReader(pageType: PageType): Reader
            = templateFile(templateName(pageType)).reader()

    private fun templateFile(name: String): File
            = File(templatesDir, "$name.mustache")

    private fun templateName(pageType: PageType): String = when (pageType) {
        PageType.Post -> "post"
    }
}

data class PageModel(
        val global: GlobalData,
        val title: String,
        val pubdate: LocalDate?,
        val content: String)

data class ParsedPost(val meta: PostMeta, val content: String)

data class PostMeta(val title: String, val pubdate: LocalDate?, val url: String, val draft: Boolean, val file: File)

data class GlobalData(val blogName: String, val posts: List<PostMeta>)