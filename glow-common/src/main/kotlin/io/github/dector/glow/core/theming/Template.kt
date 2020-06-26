package io.github.dector.glow.core.theming

import io.github.dector.glow.engine.BlogVM
import io.github.dector.glow.engine.HtmlWebPageContent
import io.github.dector.glow.engine.RenderContext
import io.github.dector.glow.plugins.notes.NoteVM

interface Template {

    fun notesIndex(notes: List<NoteVM>, context: RenderContext): HtmlWebPageContent
    fun note(blog: BlogVM, note: NoteVM): HtmlWebPageContent
    fun notesArchive(blog: BlogVM, notes: List<NoteVM>): HtmlWebPageContent

    fun tagPage(blog: BlogVM, notes: List<NoteVM>, tag: String): HtmlWebPageContent
}
