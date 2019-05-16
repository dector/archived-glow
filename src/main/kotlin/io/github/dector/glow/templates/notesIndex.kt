package io.github.dector.glow.templates

import io.github.dector.glow.plugins.notes.Note2VM
import kotlinx.html.*

fun DIV.tNotesIndexContent(notes: List<Note2VM>, title: String = "Notes", displayFullNotes: Boolean = false) {
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
    span {
        a(classes = "px-1 ml-1 border font-weight-bolder") {
            href = note.path.value

            +"..."
        }
    }
}

private fun DIV.tContent(note: Note2VM, displayFullNotes: Boolean) {
    div("text-justify") {
        unsafe {
            +(if (displayFullNotes) note.content else note.previewContent).value
        }

        if (!displayFullNotes)
            tNoteLink(note)
    }
}