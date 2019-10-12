package io.github.dector.glow.templates.bulma

import io.github.dector.glow.core.HtmlWebPageContent
import kotlinx.html.*
import kotlinx.html.stream.appendHTML

internal fun htmlPage(
    title: String,
    headExt: HEAD.() -> Unit = ::BulmaHeadExt,
    bodyExt: BODY.() -> Unit
): HtmlWebPageContent =
    StringBuilder("<!DOCTYPE html>")
        .appendln()
        .appendHTML()
        .html {
            lang = "en"

            head {
                meta(charset = "utf-8")
                meta(name = "viewport", content = "width=device-width, initial-scale=1, shrink-to-fit=no")

                title(title)

                headExt()
            }

            body {
                bodyExt()
            }
        }
        .toString()
        .let(::HtmlWebPageContent)

private fun BulmaHeadExt(head: HEAD) = head.apply {
    link("https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.5/css/bulma.css",
        rel = LinkRel.stylesheet)
}.let { Unit }
