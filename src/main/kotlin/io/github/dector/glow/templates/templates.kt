package io.github.dector.glow.templates

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.NavItemType
import io.github.dector.glow.core.NavigationItem
import io.github.dector.glow.plugins.notes.Note2VM
import io.github.dector.glow.plugins.pages.Page2VM


object Templates {

    private fun BlogVM.notesNavigationItem() = navigation.find { it.type == NavItemType.Notes }

    fun note(blog: BlogVM, note: Note2VM) = tWebPage(blog, blog.notesNavigationItem()) {
        tNoteContent(note)
    }

    fun notesIndex(blog: BlogVM, notes: List<Note2VM>) = tWebPage(blog, blog.notesNavigationItem()) {
        tNotesIndexContent(notes)
    }

    fun notesArchive(blog: BlogVM, notes: List<Note2VM>) = tWebPage(blog, blog.notesNavigationItem()) {
        tNotesIndexContent(notes, displayFullNotes = true)
    }

    fun page(blog: BlogVM, page: Page2VM, navItem: NavigationItem?) = tWebPage(blog, navItem) {
        tPageContent(page)
    }
}