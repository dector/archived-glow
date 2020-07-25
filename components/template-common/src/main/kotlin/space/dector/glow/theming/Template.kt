package space.dector.glow.theming

import space.dector.glow.engine.HtmlWebPageContent
import space.dector.glow.engine.RenderContext
import space.dector.glow.plugins.notes.NoteVM

interface Template {

    fun notesIndexPage(notes: List<NoteVM>, context: RenderContext): HtmlWebPageContent
    fun note(note: NoteVM, context: RenderContext): HtmlWebPageContent
    fun notesArchive(notes: List<NoteVM>, context: RenderContext): HtmlWebPageContent

    fun tagPage(notes: List<NoteVM>, tag: String, context: RenderContext): HtmlWebPageContent
}
