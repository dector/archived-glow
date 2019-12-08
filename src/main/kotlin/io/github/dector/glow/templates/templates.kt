package io.github.dector.glow.templates

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.config.NavItemType
import io.github.dector.glow.core.config.NavigationItem
import io.github.dector.glow.plugins.notes.Note2VM
import io.github.dector.glow.plugins.pages.Page2VM
import io.github.dector.glow.templates.hyde.notesIndexContent
import io.github.dector.glow.templates.hyde.webPage

object Templates {

    private fun BlogVM.notesNavigationItem() = navigation.find { it.type == NavItemType.Notes }

    fun notesIndex(blog: BlogVM, notes: List<Note2VM>) = webPage(blog, blog.notesNavigationItem()) {
        notesIndexContent(notes)
    }

    fun note(blog: BlogVM, note: Note2VM) = tWebPage(blog, blog.notesNavigationItem()) {
        tNoteContent(note)
    }

    fun notesArchive(blog: BlogVM, notes: List<Note2VM>) = tWebPage(blog, blog.notesNavigationItem()) {
        tNotesArchiveContent(notes)
    }

    fun page(blog: BlogVM, page: Page2VM, navItem: NavigationItem?) = tWebPage(blog, navItem) {
        tPageContent(page)
    }
}
