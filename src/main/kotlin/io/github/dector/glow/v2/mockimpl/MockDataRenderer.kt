package io.github.dector.glow.v2.mockimpl

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.html.HtmlRenderer
import io.github.dector.glow.v2.core.DataRenderer
import io.github.dector.glow.v2.core.Page
import io.github.dector.glow.v2.core.RenderedPage
import kotlinx.html.*
import kotlinx.html.stream.appendHTML

class MockDataRenderer(
        private val markdownParser: MarkdownParser<Node>
) : DataRenderer {

    private val htmlRenderer = buildRenderer()

    override fun render(page: Page): RenderedPage {
        val content = htmlRenderer.render(markdownParser.parse(page.markdownContent))

        val htmlContent = htmlPage(page.info.title) {
            unsafe {
                +content
            }
        }

        return RenderedPage(
                path = PagePath(page.info.id),
                content = htmlContent
        )
    }

    private fun buildRenderer(): HtmlRenderer = HtmlRenderer.builder().build()
}

private fun htmlPage(title: String, contentBlock: DIV.() -> Unit) = buildString {
    appendln("<!DOCTYPE html>")
    appendHTML().html {
        lang = "en"

        head {
            title(title)

            meta {
                name = "viewport"
                content = "width=device-width, initial-scale=1"
            }
            meta(charset = "UTF-8")

            link {
                rel = "stylesheet/less"
                type = "text/css"
                href = "/includes/less/style.less"
            }
        }

        body {
            div("page") {
                div("navigation") {
                    ul {
                        li { a(href = "/") { +"Home" } }
                        li { a(href = "/notes.html") { +"Notes" } }
                        li { a(href = "/projects.html") { +"Projects" } }
                        li { a(href = "/about.html") { +"About" } }
                    }
                }

                div("content") {
                    contentBlock()
                }
            }

            div("corner")

            script(src = "/includes/js/less-3.9.0.js") {}
        }
    }
}