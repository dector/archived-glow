package io.github.dector.glow.templates.hyde.layouts

import io.github.dector.glow.coordinates.Coordinates
import io.github.dector.glow.coordinates.inHostPath
import io.github.dector.glow.engine.RenderContext
import io.github.dector.glow.plugins.notes.NoteVM
import kotlinx.html.DIV
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.strong
import kotlinx.html.unsafe

fun DIV.notesIndexContent(notes: List<NoteVM>, title: String = "", context: RenderContext, displayFullNotes: Boolean = false) {
    div("posts") {
        notes.forEach { note ->
            div("post") {
                h1("post-title") {
                    a(href = note.coordinates.inHostPath()) {
                        +note.title
                    }
                }
                span("post-date") { +note.publishedAndUpdatedStr }

                noteTags(note)

                val content = if (displayFullNotes) {
                    note.content.value
                } else {
                    note.previewContent.value
                }
                unsafe { +content }

                a(href = note.coordinates.inHostPath()) {
                    strong { +"Read more..." }
                }
            }
        }
    }

    div("pagination") {
        val nextPageUrl = context.paging.nextPage
        if (nextPageUrl != null) {
            a(href = nextPageUrl.inHostPath(), classes = "pagination-item older") { +"Older" }
        } else {
            span(classes = "pagination-item older") { +"Older" }
        }

        val prevPageUrl = context.paging.prevPage
        if (prevPageUrl != null) {
            a(href = prevPageUrl.inHostPath(), classes = "pagination-item newer") { +"Newer" }
        } else {
            span(classes = "pagination-item newer") { +"Newer" }
        }
    }
}

internal fun DIV.noteTags(note: NoteVM) {
    val tags = note.rawModel.tags.takeIfNotEmpty() ?: return

    p {
        strong { +"Tags: " }

        tags.forEachIndexed { index, tag ->
            // FIXME provide in context
            val coordinates = Coordinates.Endpoint(
                "notes", "tag", tag)
            a(href = coordinates.inHostPath()) {
                +"#$tag"
            }

            if (index != tags.lastIndex) +", "
        }
    }
}

private inline fun <T : List<R>, R : Any?> T.takeIfNotEmpty(): T? =
    takeIf { it.isNotEmpty() }
