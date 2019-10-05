package io.github.dector.glow.templates.camellia

import io.github.dector.glow.core.BlogVm
import io.github.dector.glow.core.HtmlPage
import kotlinx.html.*
import kotlinx.html.stream.appendHTML

class WebPageBuilder(private val blog: BlogVm) {

    var title: String = ""

    internal fun buildHtml(): HtmlPage = StringBuilder("<!DOCTYPE html>")
        .appendln()
        .appendHTML()
        .html {
            lang = "en"

            head {
                meta(charset = "utf-8")
                meta(name = "viewport", content = "width=device-width, initial-scale=1, shrink-to-fit=no")

                title(_title())
            }

            body {
            }
        }.let { html -> "\n$html" }.let(::HtmlPage)

    private fun _title() = "${blog.title} :: $title"
}

internal fun webPage(blog: BlogVm,builder: WebPageBuilder.() -> Unit): HtmlPage =
    WebPageBuilder(blog).apply(builder).buildHtml()

/*internal fun WebPageBuilder.content(builder)*/
