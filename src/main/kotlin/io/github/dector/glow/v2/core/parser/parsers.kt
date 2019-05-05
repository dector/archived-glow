package io.github.dector.glow.v2.core.parser

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import java.io.File


interface MarkdownParser<T> {

    fun parseInsecureYFM(markdownFile: File): Map<String, String>
    fun parseInsecureYFM(markdown: String): Map<String, String>
    fun parse(markdown: String): T
}

class SimpleMarkdownParser : MarkdownParser<Node> {

    val parser = buildParser()

    private fun buildParser(): Parser {
        val parserOptions = MutableDataSet().apply {
            set(Parser.EXTENSIONS, listOf(YamlFrontMatterExtension.create()))
        }

        return Parser.builder(parserOptions).build()
    }

    override fun parseInsecureYFM(markdownFile: File): Map<String, String> {
        return parseInsecureYFM(markdownFile.readText())
    }

    override fun parseInsecureYFM(markdown: String): Map<String, String> {
        val doc = parser.parse(markdown)
        val headerData = parseYamlHeader(doc)

        return headerData
                .map { (key, value) -> key to value.joinToString(", ") }
                .toMap()
    }

    override fun parse(markdown: String): Node {
        return parser.parse(markdown)
    }

    private fun parseYamlHeader(doc: Node): Map<String, List<String>> {
        val visitor = AbstractYamlFrontMatterVisitor()
        visitor.visit(doc)

        return visitor.data
    }
}