package io.github.dector.glow.v2.dumbimpl

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.formatter.internal.Formatter
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import io.github.dector.glow.v2.core.BlogData
import io.github.dector.glow.v2.core.Post


typealias DataConverter = (List<String>) -> BlogData

val markdownFileParser: DataConverter = { data ->
    val parser = Parser.builder(MutableDataSet().apply {
        set(Parser.EXTENSIONS, listOf(YamlFrontMatterExtension.create()))
    }).build()
    val formatter = Formatter.builder().build()
    val yamlVisitor = AbstractYamlFrontMatterVisitor()

    val posts = data.map {
        val doc = parser.parse(it)
        val header = yamlVisitor.parseHeader(doc)
        val content = formatter.render(doc);

        Post(
                title = header.title,
                tags = header.tags,
                isDraft = header.isDraft,
                content = content)
    }.sortedBy { it.title } // FIXME sort by date

    BlogData(posts = posts)
}

private fun AbstractYamlFrontMatterVisitor.parseHeader(doc: Node): Header = this.run {
    visit(doc)

    val title = data["title"]
            ?.get(0) ?: ""
    val tags = data["tags"]
            ?.get(0)
            ?.split(",")
            ?.map(String::trim) ?: emptyList()
    val isDraft = data["draft"]
            ?.get(0)
            ?.toBoolean() ?: false

    return Header(
            title = title,
            tags = tags,
            isDraft = isDraft)
}

private data class Header(
        val title: String,
        val tags: List<String>,
        val isDraft: Boolean)