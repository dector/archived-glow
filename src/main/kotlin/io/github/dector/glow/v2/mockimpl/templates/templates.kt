package io.github.dector.glow.v2.mockimpl.templates

import io.github.dector.glow.v2.core.Note
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

    fun note(note: Note, content: String) = htmlPage(note.info.title) {
        h1 { +note.info.title }

        unsafe {
            +content
        }
    }
}