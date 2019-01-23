package io.github.dector.glow.v2.mockimpl.templates

import io.github.dector.glow.v2.core.Note
import io.github.dector.glow.v2.core.Page
import io.github.dector.glow.v2.core.RenderedNote
import kotlinx.html.*


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

    fun notesIndex(notes: List<RenderedNote>) = htmlPage("Notes") {
        h1 { +"Notes" }

        notes.forEach { note ->
            h3 { a(href = note.path.path) { +note.info.title } }

            p { unsafe { +note.content } }
        }
    }
}