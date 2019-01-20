package io.github.dector.glow.v2.mockimpl.templates

import io.github.dector.glow.v2.core.Page
import kotlinx.html.h1
import kotlinx.html.unsafe


object Templates {

    fun page(page: Page, content: String) = htmlPage(page.info.title) {
        h1 { +page.info.title }

        unsafe {
            +content
        }
    }
}