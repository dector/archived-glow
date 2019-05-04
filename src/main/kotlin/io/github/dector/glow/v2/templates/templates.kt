package io.github.dector.glow.v2.templates

import io.github.dector.glow.v2.core.Note2VM
import io.github.dector.glow.v2.core.Page2VM
import io.github.dector.glow.v2.implementation.NavigationItem
import kotlinx.html.*
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


object Templates {

    fun page(page: Page2VM, navigation: List<NavigationItem>) = htmlPage(page.title, navigation) {
        h1 { +page.title }

        unsafe {
            +page.content.value
        }
    }

    fun note(note: Note2VM, navigation: List<NavigationItem>) = htmlPage(note.title, navigation) {
        h1 { +note.title }

        if (note.publishedAt != null) {
            p("timestamp") {
                +note.publishedAt.formatAsMidDateTime()
            }
        }

        unsafe {
            +note.content.value
        }
    }

    fun notesIndex(notes: List<Note2VM>, navigation: List<NavigationItem>) = htmlPage("Notes", navigation) {
        h1 { +"Notes" }

        notes.forEach { note ->
            h2 {
                span("title_timestamp") { +"24 Mar 2019" }
                a(href = note.path.value) { +note.title }
            }

            p {
                unsafe { +note.previewContent.value }
                a(href = note.path.value) { +"..." }
            }
        }
    }

    fun notesArchive(notes: List<Note2VM>, navigation: List<NavigationItem>) = htmlPage("Archive", navigation) {
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