package io.github.dector.glow.templates

import io.github.dector.glow.plugins.notes.Note2VM
import kotlinx.html.DIV
import kotlinx.html.div
import kotlinx.html.p
import kotlinx.html.unsafe

fun DIV.tNoteContent(note: Note2VM) {
    tTitle(note.title)

    tPublicationDateTime(note)
    tTags(note)

    tContent(note)

    tPrevEntry(note)
    tNextEntry(note)
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

private fun DIV.tContent(note: Note2VM) {
    div("text-justify") {
        unsafe {
            +note.content.value
        }
    }
}

private fun DIV.tPrevEntry(note: Note2VM) {
    /*if (note.hasPrevious())
        +"<<"*/
}

private fun DIV.tNextEntry(note: Note2VM) {
    /*if (note.hasNext())
        +">>"*/
}