package io.github.dector.glow.v2.templates

import io.github.dector.glow.v2.mockimpl.BuildConfig.DevMode
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

            if (DevMode) {
                link {
                    rel = "stylesheet/less"
                    type = "text/css"
                    href = "/includes/less/style.less"
                }
            } else {
                link {
                    rel = "stylesheet"
                    type = "text/css"
                    href = "/includes/css/style.css"
                }
            }
        }

        body {
            div("page") {
                div("navigation") {
                    ul {
                        li { a(href = "/") { +"Home" } }
                        li { a(href = "/notes/") { +"Notes" } }
                        li { a(href = "/projects/") { +"Projects" } }
                        li { a(href = "/about/") { +"About" } }
                    }
                }

                div("content") {
                    contentBlock()
                }
            }

            div("corner")

            if (DevMode) {
                script(src = "/includes/js/less-3.9.0.js") {}
            }
        }
    }
}