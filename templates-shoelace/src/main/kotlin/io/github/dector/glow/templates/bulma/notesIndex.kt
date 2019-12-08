package io.github.dector.glow.templates.bulma

import io.github.dector.glow.plugins.notes.Note2VM
import kotlinx.html.DIV
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h3
import kotlinx.html.p
import kotlinx.html.unsafe

fun DIV.notesIndexContent(notes: List<Note2VM>, title: String = "", displayFullNotes: Boolean = false) {
    tTitle(title)

    notes.forEach { note ->
        div {
            tNoteTitle(note)
            tPublicationDateTime(note)
            tTags(note)

            tContent(note, displayFullNotes)
        }
    }
}

private fun DIV.tNoteTitle(note: Note2VM) {
    a {
        href = note.path.value

        h3 { +note.title }
    }
}

private fun DIV.tPublicationDateTime(note: Note2VM) {
    p("text-muted") {
        +note.publishedAtValue
    }
}

private fun DIV.tTags(note: Note2VM) {
    /*if (note.tags.isNotEmpty())
        p { +"Tags:" }*/
}

private fun DIV.tNoteLink(note: Note2VM) {
    p("mb-3") {
        a(classes = "font-weight-bolder") {
            href = note.path.value

            +"More"
        }
    }
}

private fun DIV.tContent(note: Note2VM, displayFullNotes: Boolean) {
    div("text-justify") {
        unsafe {
            +(if (displayFullNotes) note.content else note.previewContent).value
        }

        if (!displayFullNotes && ((note.previewContent != note.content) || note.isTrimmed))
            tNoteLink(note)
    }
}

private fun DIV.tTitle(title: String) {
    div("mb-3") {
        h1 { +title }
    }
}
