package io.github.dector.glow.v2.dumbimpl.utils

import kotlinx.html.*
import kotlinx.html.stream.appendHTML


fun htmlPage(titleStr: String, body: BODY.() -> Unit) = buildString {
    appendln("<!DOCTYPE html>")
    appendHTML().html {
        head {
            title(titleStr)
        }

        body {
            body()
        }
    }
}