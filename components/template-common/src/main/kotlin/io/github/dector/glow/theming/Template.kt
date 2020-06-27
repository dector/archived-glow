package io.github.dector.glow.theming

import io.github.dector.glow.engine.HtmlWebPageContent
import io.github.dector.glow.engine.RenderContext
import io.github.dector.glow.plugins.notes.NoteVM

interface Template {

    fun notesIndex(notes: List<NoteVM>, context: RenderContext): HtmlWebPageContent
    fun note(note: NoteVM, context: RenderContext): HtmlWebPageContent
    fun notesArchive(notes: List<NoteVM>, context: RenderContext): HtmlWebPageContent

    fun tagPage(notes: List<NoteVM>, tag: String, context: RenderContext): HtmlWebPageContent
}
