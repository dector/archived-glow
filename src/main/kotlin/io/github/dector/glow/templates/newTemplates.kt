package io.github.dector.glow.templates

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.FooterVM
import io.github.dector.glow.core.NavItemType
import io.github.dector.glow.plugins.notes.Note2VM
import kotlinx.html.*
import kotlinx.html.stream.createHTML


object Templates2 {

    fun note(blog: BlogVM, note: Note2VM) = tNote(blog, note)
}

fun tNote(blog: BlogVM, note: Note2VM) = createHTML().html {
    lang = "en"

    head {
        meta(charset = "utf-8")
        meta(name = "viewport", content = "width=device-width, initial-scale=1, shrink-to-fit=no")

        title("${blog.title} :: ${note.title}")

        link("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css",
                rel = LinkRel.stylesheet)
    }

    body {
        div("container") {
            tHeader(blog)

            tContentContainer(note)

            tFooter(blog.footer)
        }

        script(src = "https://code.jquery.com/jquery-3.3.1.slim.min.js") {}
        script(src = "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js") {}
        script(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js") {}
    }
}.let { html -> "<!DOCTYPE html>\n$html" }

fun DIV.tHeader(blog: BlogVM) {
    nav("navbar navbar-expand navbar-dark bg-dark") {
        a(classes = "navbar-brand") {
            href = "/"
            +blog.title
        }

        div("navbar-nav ml-auto") {
            blog.navigation.forEach { item ->
                a {
                    classes = setOf("nav-item", "nav-link") +
                            (if (item.type == NavItemType.Notes) "active" else "")

                    href = item.path
                    +item.title
                }
            }
        }
    }
}

fun DIV.tContentContainer(note: Note2VM) {
    div("mt-3") {
        tTitle(note)

        tPublicationDateTime(note)
        tTags(note)

        tContent(note)

        tPrevEntry(note)
        tNextEntry(note)
    }
}

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

fun DIV.tFooter(footer: FooterVM) {
    p("text-muted text-center") {
        +"${footer.author}, ${footer.year}. Content distributed under the "
        a(href = footer.licenseUrl) {
            +footer.licenseName
        }
        +"."
    }
}