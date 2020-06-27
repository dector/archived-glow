package io.github.dector.glow.templates.hyde.layouts

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
                    a(href = note.path.value) {
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

                a(href = note.path.value) {
                    strong { +"Read more..." }
                }
            }
        }
    }

    div("pagination") {
        val nextPageUrl = context.paging.nextPageUrl
        if (nextPageUrl != null) {
            a(href = nextPageUrl.value, classes = "pagination-item older") { +"Older" }
        } else {
            span(classes = "pagination-item older") { +"Older" }
        }

        val prevPageUrl = context.paging.prevPageUrl
        if (prevPageUrl != null) {
            a(href = prevPageUrl.value, classes = "pagination-item newer") { +"Newer" }
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
            a(href = tagPagePath(tag)) {
                +"#$tag"
            }

            if (index != tags.lastIndex) +", "
        }
    }
}

// FIXME provide in rendering context
private fun tagPagePath(tag: String) = "/notes/tags/$tag/"

private inline fun <T : List<R>, R : Any?> T.takeIfNotEmpty(): T? =
    takeIf { it.isNotEmpty() }
