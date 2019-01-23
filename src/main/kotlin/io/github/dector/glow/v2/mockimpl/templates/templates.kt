package io.github.dector.glow.v2.mockimpl.templates

import io.github.dector.glow.v2.core.Note2VM
import io.github.dector.glow.v2.core.Page2VM
import kotlinx.html.h1
import kotlinx.html.p
import kotlinx.html.unsafe


object Templates {

    fun page(page: Page2VM) = htmlPage(page.title) {
        h1 { +page.title }

        unsafe {
            +page.content.value
        }
    }

    fun note(note: Note2VM) = htmlPage(note.title) {
        h1 { +note.title }

        if (note.publishedAt != null) {
            p {
                +"Published: ${note.publishedAt}"
            }
        }

        unsafe {
            +note.content.value
        }
    }

    /*fun notesIndex(notes: List<RenderedNote>) = htmlPage("Notes") {
        h1 { +"Notes" }

        notes.forEach { note ->
            h3 { a(href = note.path.path) { +note.info.title } }

            p { unsafe { +note.content } }
        }
    }*/
}