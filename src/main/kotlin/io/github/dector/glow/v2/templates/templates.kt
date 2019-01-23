package io.github.dector.glow.v2.templates

import io.github.dector.glow.v2.core.Note2VM
import io.github.dector.glow.v2.core.Page2VM
import kotlinx.html.*
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


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
                +"Published: ${note.publishedAt.formatAsMidDateTime()}"
            }
        }

        unsafe {
            +note.content.value
        }
    }

    fun notesIndex(notes: List<Note2VM>) = htmlPage("Notes") {
        h1 { +"Notes" }

        notes.forEach { note ->
            h3 { a(href = note.path.value) { +note.title } }

            p { unsafe { +note.content.value } }
        }
    }

    fun notesArchive(notes: List<Note2VM>) = htmlPage("Archive") {
        h1 { +"Archive" }

        notes.forEach { note ->
            h3 { a(href = note.path.value) { +note.title } }

            p { +(note.publishedAt?.formatAsMidDateTime() ?: "") }

            p { unsafe { +note.content.value } }
        }
    }
}

private fun Instant.formatAsMidDateTime() = DateTimeFormatter
        .ofPattern("E, dd MMM uuuu HH:mm")
        .withZone(ZoneOffset.UTC)
        .format(this)