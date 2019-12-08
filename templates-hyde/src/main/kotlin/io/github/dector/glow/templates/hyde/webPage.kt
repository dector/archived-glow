package io.github.dector.glow.templates.hyde

import io.github.dector.glow.core.BlogVM
import io.github.dector.glow.core.config.NavigationItem
import kotlinx.html.DIV
import kotlinx.html.div

fun webPage(blog: BlogVM, navItem: NavigationItem?, mainContentBuilder: DIV.() -> Unit) = run {
    val pageTitle = "${blog.title} | ${navItem?.title ?: ""}"

    htmlPage(
        pageTitle,
        //bodyClasses = "layout-reverse",
        headExt = { Hyde.Includes.head(this, pageTitle) }
    ) {
        Hyde.Includes.sidebar(this, blog, navItem)

        div("content container") {
            mainContentBuilder()
        }
    }

//        pageNavigation(blog, navItem?.type)
//
//        pageContent(mainContentBuilder)
//
//        pageFooter(blog.footer)
}

//private fun BODY.pageNavigation(blog: BlogVM, pageType: NavItemType?) {
//    nav("navbar") {
//        role = "navigation"
//
//        div("container") {
//            div("navbar-brand") {
//                a(classes = "navbar-item") {
//                    href = "/"
//                    +blog.title
//                }
//            }
//        }
//
//        // TODO
//        /*div("navbar-nav ml-auto") {
//            blog.navigation.filter { it.visible }.forEach { item ->
//                a {
//                    classes = setOf("nav-item", "nav-link") +
//                        (if (item.type == pageType) "active" else "")
//
//                    href = item.path
//                    +item.title
//                }
//            }
//        }*/
//    }
//}
//
//private fun BODY.pageContent(contentBuilder: DIV.() -> Unit) {
//    div("container content") {
//        contentBuilder()
//    }
//}
//
//private fun BODY.pageFooter(footer: FooterVM) {
//    footer("footer") {
//        div("content has-text-centered") {
//            p {
//                +"${footer.author}, ${footer.year}. Content distributed under the "
//                a(href = footer.licenseUrl) {
//                    +footer.licenseName
//                }
//                +"."
//            }
//        }
//    }
////    hr {}
////
////    p("text-muted text-center") {
////
////    }
//}
