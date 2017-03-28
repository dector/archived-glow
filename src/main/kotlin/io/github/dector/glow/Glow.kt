package io.github.dector.glow

import com.beust.jcommander.JCommander
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import io.github.dector.glow.tools.StopWatch
import org.jtwig.JtwigModel
import org.jtwig.JtwigTemplate
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileFilter
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

    val opts = GlowOptions().also { JCommander(it, *args) }

    if (!OptionsValidator(opts).validate())
        return

    Glow(opts).process()

    logger.info("Executed in ${stopWatch.stop().timeFormatted()}.")
}

class Glow(val opts: GlowOptions) {

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
                url = outputFileName(file))
        return ParsedPost(meta = meta, content = content)
    }

    private fun buildPage(file: File, data: GlobalData): String {
        val post = parsePost(file)
        val glowModel = GlowModel(
                title = post.meta.title,
                pubdate = post.meta.pubdate,
                content = post.content,
                global = data)
        return renderPage(File(opts.themeDir, "page.twig"), glowModel)
    }

    private fun renderPage(templateFile: File, glowModel: GlowModel): String {
        val template = JtwigTemplate.fileTemplate(templateFile)

        val model = JtwigModel.newModel()
                .with("blogTitle", glowModel.global.blogName)
                .with("blogPosts", glowModel.global.posts)
                .with("title", glowModel.title)
                .with("pubdate", formatPubdate(glowModel.pubdate))
                .with("pubdateHint", formatPubdateHint(glowModel.pubdate))
                .with("content", glowModel.content)

        return template.render(model)
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

    private fun formatPubdate(datetime: LocalDate?): String {
        datetime ?: return ""

        return DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH).format(datetime)
    }

    private fun formatPubdateHint(datetime: LocalDate?): String {
        datetime ?: return ""

        return DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale.ENGLISH).format(datetime)
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
        for (file in postFiles) {
            writePage(outputFile(file), buildPage(file, globalData))
        }

        copyAssets()

        logger.info("Done. ${postFiles.size} file(s) proceed.")
    }
}

data class GlowModel(
        val global: GlobalData,
        val title: String,
        val pubdate: LocalDate?,
        val content: String)

data class ParsedPost(val meta: PostMeta, val content: String)

data class PostMeta(val title: String, val pubdate: LocalDate?, val url: String)

data class GlobalData(val blogName: String, val posts: List<PostMeta>)