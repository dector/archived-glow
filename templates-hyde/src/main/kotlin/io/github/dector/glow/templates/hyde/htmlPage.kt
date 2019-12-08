package io.github.dector.glow.templates.hyde

import io.github.dector.glow.core.HtmlWebPageContent
import kotlinx.html.BODY
import kotlinx.html.HEAD
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.lang
import kotlinx.html.meta
import kotlinx.html.stream.appendHTML
import kotlinx.html.title

internal fun htmlPage(
    title: String,
    headExt: HEAD.() -> Unit = {},
    bodyExt: BODY.() -> Unit
): HtmlWebPageContent =
    StringBuilder("<!DOCTYPE html>")
        .appendln()
        .appendHTML()
        .html {
            lang = "en"

            head {
                Hyde.Includes.head(this, title)

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
