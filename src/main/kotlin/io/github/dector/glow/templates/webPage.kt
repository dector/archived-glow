package io.github.dector.glow.templates

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.FooterVM
import io.github.dector.glow.core.NavItemType
import io.github.dector.glow.core.NavigationItem
import kotlinx.html.*
import kotlinx.html.stream.createHTML

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
    div("container mt-3") {
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