package io.github.dector.glow.core.builder.parser

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import io.github.dector.glow.core.builder.models.PostMeta
import java.io.File
import java.time.LocalDate

class DefaultPostParser(
        private val urlBuilder: (String) -> String) : IPostParser {

    override fun parse(file: File): ParsedPost {
        val doc = buildParser()
                .parse(file.readText())

        val header = parseYamlHeader(doc)
        val content = buildRenderer()
                .render(doc)
                .trim()

        val meta = PostMeta(
                title = header.title,
                tags = header.tags,
                isDraft = header.isDraft,
                file = file,
                url = urlBuilder(file.nameWithoutExtension),
                pubDate = postPubDateFromFilename(file.nameWithoutExtension))
        return ParsedPost(
                meta = meta,
                content = content)
    }

    private fun buildParser(): Parser {
        val parserOptions = MutableDataSet().apply {
            set(Parser.EXTENSIONS, listOf(YamlFrontMatterExtension.create()))
        }

        return Parser.builder(parserOptions).build()
    }

    private fun buildRenderer(): HtmlRenderer = HtmlRenderer.builder().build()

    private fun parseYamlHeader(doc: Node): Header {
        val visitor = AbstractYamlFrontMatterVisitor()
        visitor.visit(doc)

        val title = visitor.data["title"]
                ?.get(0) ?: ""
        val tags = visitor.data["tags"]
                ?.get(0)
                ?.split(",")
                ?.map(String::trim) ?: emptyList()
        val isDraft = visitor.data["draft"]
                ?.get(0)
                ?.toBoolean() ?: false

        return Header(
                title = title,
                tags = tags,
                isDraft = isDraft)
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

    private data class Header(
            val title: String,
            val tags: List<String>,
            val isDraft: Boolean)
}