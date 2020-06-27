package io.github.dector.glow.engine

import io.github.dector.glow.coordinates.Coordinates

inline class MarkdownContent(val value: String)
inline class HtmlContent(val value: String)
inline class HtmlWebPageContent(val value: String)

@Deprecated("Use `Coordinates`")
inline class WebPagePath(val value: String) {
    companion object
}

data class RenderedWebPage(
    val coordinates: Coordinates.Endpoint,
    val content: HtmlWebPageContent
)

@Deprecated("Use `RenderedWebPage`")
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
    val description: String = "",
    val footer: FooterVM = FooterVM(),
    val navigation: List<NavItemVM> = emptyList()
)

data class FooterVM(
    val author: String = "",
    val year: String = "",
    val licenseName: String = "",
    val licenseUrl: String = ""
)

data class NavItemVM(
    val title: String = "",
    val path: String = ""
)

data class RssFeed(val filePath: String, val content: String)
