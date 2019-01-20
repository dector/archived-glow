package io.github.dector.glow.v2.mockimpl

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import java.io.File


interface MarkdownParser {

    fun parseInsecureYFM(markdownFile: File): Map<String, String>
}

class SimpleMarkdownParser : MarkdownParser {

    val parser = buildParser()

    private fun buildParser(): Parser {
        val parserOptions = MutableDataSet().apply {
            set(Parser.EXTENSIONS, listOf(YamlFrontMatterExtension.create()))
        }

        return Parser.builder(parserOptions).build()
    }

    override fun parseInsecureYFM(markdownFile: File): Map<String, String> {
        val doc = parser.parse(markdownFile.readText())
        val headerData = parseYamlHeader(doc)

        return headerData
                .map { (key, value) -> key to value.joinToString(", ") }
                .toMap()
    }

    private fun parseYamlHeader(doc: Node): Map<String, List<String>> {
        val visitor = AbstractYamlFrontMatterVisitor()
        visitor.visit(doc)

        return visitor.data
    }
}