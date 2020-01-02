package io.github.dector.glow.templates.hyde.layouts

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.plugins.notes.NoteVM
import kotlinx.html.DIV
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.span
import kotlinx.html.unsafe

fun DIV.noteContent(blog: BlogVM, note: NoteVM) {
    div("post") {
        h1("post-title") { +note.title }
        span(classes = "post-date") { +note.publishedAndUpdatedStr }

        unsafe {
            +note.content.value
        }
    }

    /*div(classes = "related") {
        h2 { +"""Related Posts""" }
        ul(classes = "related-posts") {
            +"""{% for post in site.related_posts limit:3 %}"""
            li {
                h3 {
                    a {
                        href = "{{ post.url }}"
                        +"""{{ post.title }}"""
                        small { +"""{{ post.date | date_to_string }}""" }
                    }
                }
            }
            +"""{% endfor %}"""
        }
    }*/
}
