package io.github.dector.glow.v2.core.components

import io.github.dector.glow.v2.core.Note2
import io.github.dector.glow.v2.core.Page2
import io.github.dector.glow.v2.pipeline.NotesDataProvider

interface DataProvider : NotesDataProvider {

    fun fetchPages(): List<Page2>
    override fun fetchNotes(): List<Note2>
}