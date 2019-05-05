package io.github.dector.glow.v2.core.components

import io.github.dector.glow.v2.core.Note2
import io.github.dector.glow.v2.core.Page2
import io.github.dector.glow.v2.core.WebPage
import io.github.dector.glow.v2.pipeline.NotesDataRenderer

interface DataRenderer : NotesDataRenderer {

    fun render(page: Page2): WebPage
    override fun render(note: Note2): WebPage
    override fun renderNotesIndex(notes: List<Note2>): WebPage
    override fun renderNotesArchive(notes: List<Note2>): WebPage
}