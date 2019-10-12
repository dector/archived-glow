package io.github.dector.glow.core

import io.github.dector.glow.core.config.NavigationItem

inline class MarkdownContent(val value: String)
inline class HtmlContent(val value: String)
inline class HtmlWebPageContent(val value: String)
inline class WebPagePath(val value: String) {
    companion object
}

data class WebPage(
    val path: WebPagePath,
    val content: HtmlWebPageContent
)

val WebPagePath.isLost: Boolean get() = value.isEmpty()
val WebPagePath.Companion.Empty get() = WebPagePath("")
val WebPagePath.isIndex: Boolean
    get() = (value == "index.html") || (value.endsWith("/index.html"))

data class BlogVM(
    val title: String = "",
    val footer: FooterVM = FooterVM(),
    val navigation: List<NavigationItem> = emptyList()
)

data class FooterVM(
    val author: String = "",
    val year: String = "",
    val licenseName: String = "",
    val licenseUrl: String = ""
)
