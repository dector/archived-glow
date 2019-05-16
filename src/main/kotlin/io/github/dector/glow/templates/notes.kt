package io.github.dector.glow.templates

import io.github.dector.glow.plugins.notes.Note2VM
import kotlinx.html.*

fun DIV.tNoteContent(note: Note2VM) {
    fun DIV.tTitle(note: Note2VM) {
        h1 { +note.title }
    }

    fun DIV.tPublicationDateTime(note: Note2VM) {
        p("text-muted") {
            +note.publishedAtValue
        }
    }

    fun DIV.tTags(note: Note2VM) {
        /*if (note.tags.isNotEmpty())
            p { +"Tags:" }*/
    }

    fun DIV.tContent(note: Note2VM) {
        div("text-justify") {
            unsafe {
                +note.content.value
            }
        }
    }

    fun DIV.tPrevEntry(note: Note2VM) {
        /*if (note.hasPrevious())
            +"<<"*/
    }

    fun DIV.tNextEntry(note: Note2VM) {
        /*if (note.hasNext())
            +">>"*/
    }

    tTitle(note)

    tPublicationDateTime(note)
    tTags(note)

    tContent(note)

    tPrevEntry(note)
    tNextEntry(note)
}