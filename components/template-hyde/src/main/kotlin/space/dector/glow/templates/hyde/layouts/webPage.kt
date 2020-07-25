package space.dector.glow.templates.hyde.layouts

import kotlinx.html.DIV
import kotlinx.html.div
import space.dector.glow.engine.BlogVM
import space.dector.glow.engine.NavItemVM
import space.dector.glow.engine.RenderContext
import space.dector.glow.templates.hyde.Hyde

fun webPage(context: RenderContext, titleAddition: String? = null, mainContentBuilder: DIV.() -> Unit) =
    webPage(context.blog, context.currentNavSection, titleAddition, mainContentBuilder)

fun webPage(blog: BlogVM, currentSection: NavItemVM, titleAddition: String? = null, mainContentBuilder: DIV.() -> Unit) = run {
    val pageTitle = "${blog.title} | ${currentSection.title}" +
        (if (titleAddition != null) " | $titleAddition" else "")

    htmlPage(
        pageTitle,
        //bodyClasses = "layout-reverse",
        headExt = { Hyde.Includes.head(this, pageTitle) }
    ) {
        Hyde.Includes.sidebar(this, blog, currentSection)

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
