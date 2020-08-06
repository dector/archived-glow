package space.dector.glow.theming

import space.dector.glow.engine.HtmlWebPageContent
import space.dector.glow.engine.RenderContext
import space.dector.glow.plugins.notes.NoteVM
import space.dector.glow.plugins.notes.TagVM

interface Template {

    fun notesIndexPage(notes: List<NoteVM>, context: RenderContext): HtmlWebPageContent
    fun note(note: NoteVM, context: RenderContext): HtmlWebPageContent
    fun notesArchive(notes: List<NoteVM>, context: RenderContext): HtmlWebPageContent

    @Deprecated("Use new method")
    fun tagPage(notes: List<NoteVM>, tag: String, context: RenderContext): HtmlWebPageContent = error("Use new method")
    fun tagPage(notes: List<NoteVM>, tag: TagVM, context: RenderContext): HtmlWebPageContent
}
