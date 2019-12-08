package io.github.dector.glow.templates.hyde

import io.github.dector.glow.plugins.notes.Note2VM
import kotlinx.html.DIV
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h3
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.strong
import kotlinx.html.unsafe

fun DIV.notesIndexContent(notes: List<Note2VM>, title: String = "", displayFullNotes: Boolean = false) {
    div("posts") {
        notes.forEach { note ->
            div("post") {
                h1("post-title") {
                    a(href = note.path.value) {
                        +note.title
                    }
                }
                span("post-date") { +note.publishedAtValue }

                val content = if (displayFullNotes) {
                    note.content.value
                } else {
                    note.previewContent.value
                }
                unsafe { +content }

                a(href = note.path.value) {
                    strong { +"Read more..." }
                }
            }
        }
    }

    div("pagination") {
        //        +"""{% if paginator.next_page %}"""
//        a(href = "{{ site.baseurl }}page{{paginator.next_page}}") {
//            classes = setOf("pagination-item", "older")
//
//            +"""Older"""
//        }
//        +"""{% else %}"""
//        span(classes = "pagination-item older") { +"""Older""" }
//        +"""{% endif %}
//  {% if paginator.previous_page %}
//    {% if paginator.page == 2 %}"""
//        a(classes = "pagination-item newer") {
//            href = "{{ site.baseurl }}"
//            +"""Newer"""
//        }
//        +"""{% else %}"""
//        a(classes = "pagination-item newer") {
//            href = "{{ site.baseurl }}page{{paginator.previous_page}}"
//            +"""Newer"""
//        }
//        +"""{% endif %}
//  {% else %}"""
//        span(classes = "pagination-item newer") { +"""Newer""" }
//        +"""{% endif %}"""
    }

    /*tTitle(title)

    notes.forEach { note ->
        div {
            tNoteTitle(note)
            tPublicationDateTime(note)
            tTags(note)

            tContent(note, displayFullNotes)
        }
    }*/
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
