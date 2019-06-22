package io.github.dector.glow.templates

import io.github.dector.glow.plugins.notes.Note2VM
import kotlinx.html.*
import java.time.Month
import java.time.Year
import java.time.ZoneOffset

fun DIV.tNotesArchiveContent(notes: List<Note2VM>) {
    tTitle("Archive")

    val sortedNotes = notes.sortedWith(
        compareBy({
            it.publishedAt
                ?.atOffset(ZoneOffset.UTC)
                ?.year
                ?: Year.MIN_VALUE
        }, {
            it.publishedAt
                ?.atOffset(ZoneOffset.UTC)
                ?.month
                ?: Month.JANUARY
        }, {
            it.publishedAt
                ?.atOffset(ZoneOffset.UTC)
                ?.dayOfMonth
                ?: 1
        })
    ).reversed()

    sortedNotes.forEach { note ->
        tNoteTitle(note)
    }
}

private fun DIV.tNoteTitle(note: Note2VM) {
    p {
        tPublicationDateTime(note)

        a {
            href = note.path.value

            +note.title
        }
    }
}

private fun P.tPublicationDateTime(note: Note2VM) {
    span("text-muted") {
        +"${note.publishedAtValue} - "
    }
}
