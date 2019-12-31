package io.github.dector.glow.templates

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.config.NavItemType
import io.github.dector.glow.core.theming.Theme
import io.github.dector.glow.plugins.notes.Note2VM
import io.github.dector.glow.templates.hyde.HydeTheme

object Templates {

    private val theme: Theme = HydeTheme()

    private fun BlogVM.notesNavigationItem() = navigation.find { it.type == NavItemType.Notes }

    fun notesIndex(blog: BlogVM, notes: List<Note2VM>) =
        theme.notesIndex(blog, notes)

    fun note(blog: BlogVM, note: Note2VM) =
        theme.note(blog, note)

    fun notesArchive(blog: BlogVM, notes: List<Note2VM>): String = TODO()

}
