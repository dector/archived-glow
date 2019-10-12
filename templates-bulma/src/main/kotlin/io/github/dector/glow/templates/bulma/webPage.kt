package io.github.dector.glow.templates.bulma

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.FooterVM
import io.github.dector.glow.core.config.NavItemType
import io.github.dector.glow.core.config.NavigationItem
import kotlinx.html.*

fun webPage(blog: BlogVM, navItem: NavigationItem?, mainContentBuilder: DIV.() -> Unit) =
    htmlPage("${blog.title} | ${navItem?.title ?: ""}") {
        pageNavigation(blog, navItem?.type)

        pageContent(mainContentBuilder)

        pageFooter(blog.footer)
    }

private fun BODY.pageNavigation(blog: BlogVM, pageType: NavItemType?) {
    nav("navbar") {
        role = "navigation"

        div("container") {
            div("navbar-brand") {
                a(classes = "navbar-item") {
                    href = "/"
                    +blog.title
                }
            }
        }

        // TODO
        /*div("navbar-nav ml-auto") {
            blog.navigation.filter { it.visible }.forEach { item ->
                a {
                    classes = setOf("nav-item", "nav-link") +
                        (if (item.type == pageType) "active" else "")

                    href = item.path
                    +item.title
                }
            }
        }*/
    }
}

private fun BODY.pageContent(contentBuilder: DIV.() -> Unit) {
    div("container content") {
        contentBuilder()
    }
}

private fun BODY.pageFooter(footer: FooterVM) {
    footer("footer") {
        div("content has-text-centered") {
            p {
                +"${footer.author}, ${footer.year}. Content distributed under the "
                a(href = footer.licenseUrl) {
                    +footer.licenseName
                }
                +"."
            }
        }
    }
//    hr {}
//
//    p("text-muted text-center") {
//
//    }
}
