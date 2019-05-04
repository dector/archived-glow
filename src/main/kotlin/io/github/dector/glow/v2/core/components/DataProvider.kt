package io.github.dector.glow.v2.core.components

import io.github.dector.glow.v2.core.Note2
import io.github.dector.glow.v2.core.Page2

interface DataProvider {

    fun fetchPages(): List<Page2>
    fun fetchNotes(): List<Note2>
}