package io.github.dector.glow

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import org.jtwig.JtwigModel
import org.jtwig.JtwigTemplate

class Glow {

}

fun main(args: Array<String>) {
    val content = buildContent()
    val options = Options(
            title = "Title",
            content = content)
    val html = buildPage(options)

    println(html)
}

fun buildContent(): String {
    val parser = Parser.builder().build()
    val renderer = HtmlRenderer.builder().build()

    val doc = parser.parse("Markdown `rocks`")
    return renderer.render(doc).trim()
}

fun buildPage(options: Options): String {
    val template = JtwigTemplate.inlineTemplate("""
            |<html>
            |<head>
            |    <title>{{ title }}</title>
            |</head>
            |<body>
            |    {{ content }}
            |</body>
            |</html>
            |""".trimMargin())

    val model = JtwigModel.newModel()
            .with("title", options.title)
            .with("content", options.content)

    return template.render(model)
}

data class Options(
        val title: String,
        val content: String)