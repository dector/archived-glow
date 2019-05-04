package io.github.dector.glow.v2.core.components

import io.github.dector.glow.v2.core.Note2
import io.github.dector.glow.v2.core.Page2
import io.github.dector.glow.v2.core.WebPagePath


interface PathResolver {

    fun resolve(page: Page2): WebPagePath
    fun resolve(note: Note2): WebPagePath
    fun resolveNotesIndex(): WebPagePath
    fun resolveNotesArchive(): WebPagePath
}