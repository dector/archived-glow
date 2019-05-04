package io.github.dector.glow.v2.core.components

import io.github.dector.glow.v2.core.Note2
import io.github.dector.glow.v2.core.Page2
import io.github.dector.glow.v2.core.WebPage

interface DataRenderer {

    fun render(page: Page2): WebPage
    fun render(note: Note2): WebPage
    fun renderNotesIndex(notes: List<Note2>): WebPage
    fun renderNotesArchive(notes: List<Note2>): WebPage
}