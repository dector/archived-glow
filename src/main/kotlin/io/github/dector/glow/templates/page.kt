package io.github.dector.glow.templates

import io.github.dector.glow.plugins.pages.Page2VM
import kotlinx.html.DIV
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.unsafe

fun DIV.tPageContent(page: Page2VM) {
    tTitle(page)

    tContent(page)
}

private fun DIV.tTitle(page: Page2VM) {
    h1 { +page.title }
}

private fun DIV.tContent(page: Page2VM) {
    div("text-justify") {
        unsafe {
            +page.content.value
        }
    }
}