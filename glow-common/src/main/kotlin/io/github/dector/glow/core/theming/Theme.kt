package io.github.dector.glow.core.theming

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.HtmlWebPageContent
import io.github.dector.glow.core.components.RenderContext
import io.github.dector.glow.plugins.notes.NoteVM

interface Theme {

    fun notesIndex(notes: List<NoteVM>, context: RenderContext): HtmlWebPageContent
    fun note(blog: BlogVM, note: NoteVM): HtmlWebPageContent
    fun notesArchive(blog: BlogVM, notes: List<NoteVM>): HtmlWebPageContent

    fun tagPage(blog: BlogVM, notes: List<NoteVM>, tag: String): HtmlWebPageContent
}
