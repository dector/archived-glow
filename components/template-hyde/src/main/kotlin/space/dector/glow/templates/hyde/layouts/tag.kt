package space.dector.glow.templates.hyde.layouts

import kotlinx.html.DIV
import space.dector.glow.engine.RenderContext
import space.dector.glow.plugins.notes.NoteVM

fun DIV.tagPageContent(notes: List<NoteVM>, tag: String, context: RenderContext) =
    notesIndexContent(notes, context = context)
