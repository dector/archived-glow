package io.github.dector.glow.templates.bulma

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.FooterVM
import io.github.dector.glow.core.config.NavItemType
import io.github.dector.glow.core.config.NavigationItem
import kotlinx.html.*

fun webPage(blog: BlogVM, navItem: NavigationItem?, mainContentBuilder: DIV.() -> Unit) =
    htmlPage("${blog.title} | ${navItem?.title ?: ""}") {
        section("section") {
            div("container") {
                pageHeader(blog, navItem?.type)

                pageContentContainer(mainContentBuilder)

                pageFooter(blog.footer)
            }
        }
    }

private fun DIV.pageHeader(blog: BlogVM, pageType: NavItemType?) {
    nav("navbar navbar-expand") {
        a(classes = "navbar-brand") {
            href = "/"
            +blog.title
        }

        div("navbar-nav ml-auto") {
            blog.navigation.filter { it.visible }.forEach { item ->
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

private fun DIV.pageContentContainer(contentBuilder: DIV.() -> Unit) {
    div("container my-4") {
        contentBuilder()
    }
}

private fun DIV.pageFooter(footer: FooterVM) {
    hr {}

    p("text-muted text-center") {
        +"${footer.author}, ${footer.year}. Content distributed under the "
        a(href = footer.licenseUrl) {
            +footer.licenseName
        }
        +"."
    }
}
