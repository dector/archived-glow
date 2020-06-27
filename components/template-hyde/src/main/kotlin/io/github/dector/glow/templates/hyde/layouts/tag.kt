package io.github.dector.glow.templates.hyde.layouts

import io.github.dector.glow.engine.RenderContext
import io.github.dector.glow.plugins.notes.NoteVM
import kotlinx.html.DIV

fun DIV.tagPageContent(notes: List<NoteVM>, tag: String, context: RenderContext) =
    notesIndexContent(notes, context = context)
