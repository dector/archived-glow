package io.github.dector.glow.v2.dumbimpl

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.formatter.internal.Formatter
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import io.github.dector.glow.v2.BlogData
import io.github.dector.glow.v2.models.Post


typealias DataConverter = (List<String>) -> BlogData

val mdFileParser: DataConverter = { data ->
    fun buildParser() = Parser.builder(MutableDataSet().apply {
        set(Parser.EXTENSIONS, listOf(YamlFrontMatterExtension.create()))
    }).build()

    fun parseYamlHeader(doc: Node): Header {
        return AbstractYamlFrontMatterVisitor().run {
            visit(doc)

            val title = this.data["title"]
                    ?.get(0) ?: ""
            val tags = this.data["tags"]
                    ?.get(0)
                    ?.split(",")
                    ?.map(String::trim) ?: emptyList()
            val isDraft = this.data["draft"]
                    ?.get(0)
                    ?.toBoolean() ?: false

            return Header(
                    title = title,
                    tags = tags,
                    isDraft = isDraft)
        }
    }

    val parser = buildParser()
    val formatter = Formatter.builder().build()

    val posts = data.map {
        val doc = parser.parse(it)
        val header = parseYamlHeader(doc)
        val content = formatter.render(doc);

        Post(title = header.title, tags = header.tags, isDraft = header.isDraft, content = content)
    }.sortedBy { it.title } // FIXME sort by date

    BlogData(posts = posts)
}

private data class Header(
        val title: String,
        val tags: List<String>,
        val isDraft: Boolean)