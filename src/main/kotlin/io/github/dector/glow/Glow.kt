package io.github.dector.glow

import com.beust.jcommander.JCommander
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
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
    println(cliHeader())

    val opts = GlowOptions().also { JCommander(it, *args) }

    if (!OptionsValidator(opts).validate())
        return

    Glow(opts).process()
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

    private fun writePage(file: File, html: String) {
        file.writeText(html)
    }

    private fun outputFile(inputFile: File)
            = File(opts.outputDir, "${inputFile.nameWithoutExtension}.html")

    private fun buildPage(file: File): String {
        val content = buildContent(file)
        val datetime = dateTimeFromFilename(file.nameWithoutExtension)
        val glowModel = GlowModel(
                title = content.title,
                datetime = datetime,
                content = content.content)
        return buildPage(File(opts.themeDir, "page.twig"), glowModel)
    }

    private fun buildContent(file: File): ParsedContent {
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

        return ParsedContent(
                title = yamlVisitor.data["title"]?.get(0) ?: "",
                content = content)
    }

    private fun buildPage(templateFile: File, glowModel: GlowModel): String {
        val template = JtwigTemplate.fileTemplate(templateFile)

        val model = JtwigModel.newModel()
                .with("title", glowModel.title)
                .with("datetime", formatDatetime(glowModel.datetime))
                .with("datetimeShort", formatShortDatetime(glowModel.datetime))
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

    private fun formatDatetime(datetime: LocalDate?): String {
        datetime ?: return ""

        return DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH).format(datetime)
    }

    private fun formatShortDatetime(datetime: LocalDate?): String {
        datetime ?: return ""

        return DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale.ENGLISH).format(datetime)
    }

    fun process() {
        prepareDirs()

        val postFiles = listPostFiles()

        logger.info("${postFiles.size} posts found.")

        logger.info("Building posts.")
        for (file in postFiles) {
            writePage(outputFile(file), buildPage(file))
        }

        copyAssets()

        logger.info("Done. ${postFiles.size} file(s) proceed.")
    }
}

data class GlowModel(
        val title: String,
        val datetime: LocalDate?,
        val content: String)

data class ParsedContent(val title: String, val content: String)