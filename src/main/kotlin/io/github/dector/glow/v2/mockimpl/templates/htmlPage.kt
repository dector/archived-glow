package io.github.dector.glow.v2.mockimpl.templates

import kotlinx.html.*
import kotlinx.html.stream.appendHTML

fun htmlPage(title: String, contentBlock: DIV.() -> Unit) = buildString {
    appendln("<!DOCTYPE html>")
    appendHTML().html {
        lang = "en"

        head {
            title("Dead Art Space :: $title")

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
                        li { a(href = "/notes/") { +"Notes" } }
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