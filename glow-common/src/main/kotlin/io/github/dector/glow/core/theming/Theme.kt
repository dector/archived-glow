package io.github.dector.glow.core.theming

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.HtmlWebPageContent
import io.github.dector.glow.plugins.notes.Note2VM

interface Theme {

    fun notesIndex(blog: BlogVM, notes: List<Note2VM>): HtmlWebPageContent
    fun note(blog: BlogVM, note: Note2VM): HtmlWebPageContent
}
