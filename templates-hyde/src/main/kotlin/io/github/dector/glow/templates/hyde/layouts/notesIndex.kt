package io.github.dector.glow.templates.hyde.layouts

import io.github.dector.glow.plugins.notes.NoteVM
import io.github.dector.glow.utils.takeIfNotEmpty
import kotlinx.html.DIV
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.strong
import kotlinx.html.unsafe

fun DIV.notesIndexContent(notes: List<NoteVM>, title: String = "", displayFullNotes: Boolean = false) {
    div("posts") {
        notes.forEach { note ->
            div("post") {
                h1("post-title") {
                    a(href = note.path.value) {
                        +note.title
                    }
                }
                span("post-date") { +note.publishedAndUpdatedStr }

                val content = if (displayFullNotes) {
                    note.content.value
                } else {
                    note.previewContent.value
                }
                unsafe { +content }

                noteTags(note)

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
}

private fun DIV.noteTags(note: NoteVM) {
    val tags = note.rawModel.tags.takeIfNotEmpty() ?: return

    p {
        strong { +"Tags: " }

        +tags.joinToString()
    }
}
