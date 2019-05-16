package io.github.dector.glow.templates

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.FooterVM
import io.github.dector.glow.core.NavItemType
import io.github.dector.glow.core.NavigationItem
import io.github.dector.glow.plugins.notes.Note2VM
import io.github.dector.glow.plugins.pages.Page2VM
import kotlinx.html.*
import kotlinx.html.stream.createHTML


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

fun tWebPage(blog: BlogVM, navItem: NavigationItem?, mainContentBuilder: DIV.() -> Unit) = createHTML().html {
    lang = "en"

    head {
        meta(charset = "utf-8")
        meta(name = "viewport", content = "width=device-width, initial-scale=1, shrink-to-fit=no")

        title("${blog.title} :: ${navItem?.title ?: ""}")

        link("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css",
                rel = LinkRel.stylesheet)
    }

    body {
        div("container") {
            tHeader(blog, navItem?.type)

            tContentContainer(mainContentBuilder)

            tFooter(blog.footer)
        }

        script(src = "https://code.jquery.com/jquery-3.3.1.slim.min.js") {}
        script(src = "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js") {}
        script(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js") {}
    }
}.let { html -> "<!DOCTYPE html>\n$html" }

fun DIV.tHeader(blog: BlogVM, pageType: NavItemType?) {
    nav("navbar navbar-expand navbar-dark bg-dark") {
        a(classes = "navbar-brand") {
            href = "/"
            +blog.title
        }

        div("navbar-nav ml-auto") {
            blog.navigation.forEach { item ->
                a {
                    classes = setOf("nav-item", "nav-link") +
                            (if (item.type == pageType) "active" else "")

                    href = item.path
                    +item.title
                }
            }
        }
    }
}

fun DIV.tContentContainer(contentBuilder: DIV.() -> Unit) {
    div("mt-3") {
        contentBuilder()
    }
}

fun DIV.tFooter(footer: FooterVM) {
    p("text-muted text-center") {
        +"${footer.author}, ${footer.year}. Content distributed under the "
        a(href = footer.licenseUrl) {
            +footer.licenseName
        }
        +"."
    }
}

// Notes

fun DIV.tNoteContent(note: Note2VM) {
    fun DIV.tTitle(note: Note2VM) {
        h1 { +note.title }
    }

    fun DIV.tPublicationDateTime(note: Note2VM) {
        p("text-muted") {
            +note.publishedAtValue
        }
    }

    fun DIV.tTags(note: Note2VM) {
        /*if (note.tags.isNotEmpty())
            p { +"Tags:" }*/
    }

    fun DIV.tContent(note: Note2VM) {
        div("text-justify") {
            unsafe {
                +note.content.value
            }
        }
    }

    fun DIV.tPrevEntry(note: Note2VM) {
        /*if (note.hasPrevious())
            +"<<"*/
    }

    fun DIV.tNextEntry(note: Note2VM) {
        /*if (note.hasNext())
            +">>"*/
    }

    tTitle(note)

    tPublicationDateTime(note)
    tTags(note)

    tContent(note)

    tPrevEntry(note)
    tNextEntry(note)
}

fun DIV.tNotesIndexContent(notes: List<Note2VM>, title: String = "Notes", displayFullNotes: Boolean = false) {
    fun DIV.tTitle(title: String) {
        h1 { +title }
    }

    fun DIV.tNoteTitle(note: Note2VM) {
        a {
            href = note.path.value

            h3 { +note.title }
        }
    }

    fun DIV.tPublicationDateTime(note: Note2VM) {
        p("text-muted") {
            +note.publishedAtValue
        }
    }

    fun DIV.tTags(note: Note2VM) {
        /*if (note.tags.isNotEmpty())
            p { +"Tags:" }*/
    }

    fun DIV.tNoteLink(note: Note2VM) {
        span {
            a(classes = "text-light bg-dark font-weight-bold h6 px-1 ml-2") {
                href = note.path.value

                +"..."
            }
        }
    }

    fun DIV.tContent(note: Note2VM, displayFullNotes: Boolean) {
        div("text-justify") {
            unsafe {
                +(if (displayFullNotes) note.content else note.previewContent).value
            }

            if (!displayFullNotes)
                tNoteLink(note)
        }
    }

    tTitle(title)

    notes.forEach { note ->
        div {
            tNoteTitle(note)
            tPublicationDateTime(note)
            tTags(note)

            tContent(note, displayFullNotes)
        }
    }
}

// Pages

fun DIV.tPageContent(page: Page2VM) {
    fun DIV.tTitle(page: Page2VM) {
        h1 { +page.title }
    }

    fun DIV.tContent(page: Page2VM) {
        div("text-justify") {
            unsafe {
                +page.content.value
            }
        }
    }

    tTitle(page)

    tContent(page)
}