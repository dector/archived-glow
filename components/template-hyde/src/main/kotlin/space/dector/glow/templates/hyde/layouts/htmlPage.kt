package space.dector.glow.templates.hyde.layouts

import kotlinx.html.BODY
import kotlinx.html.HEAD
import kotlinx.html.body
import kotlinx.html.classes
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.lang
import kotlinx.html.meta
import kotlinx.html.stream.appendHTML
import kotlinx.html.title
import kotlinx.html.unsafe
import space.dector.glow.engine.HtmlWebPageContent

internal fun htmlPage(
    title: String,
    bodyClasses: String? = null,
    headExt: HEAD.() -> Unit = {},
    bodyExt: BODY.() -> Unit
): HtmlWebPageContent =
    StringBuilder("<!DOCTYPE html>")
        .appendln()
        .appendHTML()
        .html {
            lang = "en"

            head {
                meta(charset = "utf-8")
                //meta(name = "viewport", content = "width=device-width, initial-scale=1, shrink-to-fit=no")

                title { +title }
                unsafe { +"<!-- Build with 'glow' (non-public yet) using Hyde theme (https://github.com/poole/hyde). -->" }

                headExt()
            }

            body {
                bodyClasses?.split(" ")?.let { classes = it.toSet() }

                bodyExt()
            }
        }
        .toString()
        .let(::HtmlWebPageContent)
