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
        val glowModel = GlowModel(
                title = content.title,
                content = content.content)
        val html = buildPage(File(opts.themeDir, "page.twig"), glowModel)

        File(opts.outputDir, "${file.nameWithoutExtension}.html")
                .writeText(html)
    }

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
            .with("content", glowModel.content)

    return template.render(model)
}

data class GlowModel(
        val title: String,
        val content: String)

data class ParsedContent(val title: String, val content: String)