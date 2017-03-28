package io.github.dector.glow

import com.beust.jcommander.JCommander
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import org.jtwig.JtwigModel
import org.jtwig.JtwigTemplate
import java.io.File
import java.io.FileFilter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun main(args: Array<String>) {
    val opts = GlowOptions().also { JCommander(it, *args) }

    opts.outputDir?.deleteRecursively() // Dev purposes

    if (!OptionsValidator(opts).validate())
        return

    opts.outputDir?.mkdirs()

    val postFiles = File(opts.inputDir, "posts/")
            .listFiles(FileFilter { it.extension == "md" })
    for (file in postFiles) {
        val content = buildContent(file)
        val datetime = dateTimeFromFilename(file.nameWithoutExtension)
        val glowModel = GlowModel(
                title = content.title,
                datetime = datetime,
                content = content.content)
        val html = buildPage(File(opts.themeDir, "page.twig"), glowModel)

        File(opts.outputDir, "${file.nameWithoutExtension}.html")
                .writeText(html)
    }

    File(opts.themeDir, "assets").copyRecursively(File(opts.outputDir, "assets"))

    println("Done. ${postFiles.size} file(s) proceed")
}

fun buildContent(file: File): ParsedContent {
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

fun buildPage(templateFile: File, glowModel: GlowModel): String {
    val template = JtwigTemplate.fileTemplate(templateFile)

    val model = JtwigModel.newModel()
            .with("title", glowModel.title)
            .with("datetime", formatDatetime(glowModel.datetime))
            .with("datetimeShort", formatShortDatetime(glowModel.datetime))
            .with("content", glowModel.content)

    return template.render(model)
}

fun dateTimeFromFilename(name: String): LocalDate? {
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

fun formatDatetime(datetime: LocalDate?): String {
    datetime ?: return ""

    return DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH).format(datetime)
}

fun formatShortDatetime(datetime: LocalDate?): String {
    datetime ?: return ""

    return DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale.ENGLISH).format(datetime)
}

data class GlowModel(
        val title: String,
        val datetime: LocalDate?,
        val content: String)

data class ParsedContent(val title: String, val content: String)