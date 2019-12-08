package io.github.dector.glow.templates.bulma

import io.github.dector.glow.core.HtmlWebPageContent
import kotlinx.html.BODY
import kotlinx.html.HEAD
import kotlinx.html.LinkRel
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.lang
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.stream.appendHTML
import kotlinx.html.title

internal fun htmlPage(
    title: String,
    headExt: HEAD.() -> Unit = ::ShoelaceHeadExt,
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

private fun ShoelaceHeadExt(head: HEAD) = head.apply {
    link("https://cdn.shoelace.style/1.0.0-beta24/shoelace.css",
        rel = LinkRel.stylesheet)
}.let { Unit }
