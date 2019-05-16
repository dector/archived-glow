package io.github.dector.glow.templates

import io.github.dector.glow.plugins.notes.Note2VM
import kotlinx.html.*

fun DIV.tNoteContent(note: Note2VM) {
    tTitle(note)

    tPublicationDateTime(note)
    tTags(note)

    tContent(note)

    tPrevEntry(note)
    tNextEntry(note)
}

private fun DIV.tTitle(note: Note2VM) {
    h1 { +note.title }
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