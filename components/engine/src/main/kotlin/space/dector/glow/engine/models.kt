package space.dector.glow.engine

import space.dector.glow.coordinates.Coordinates

inline class MarkdownContent(val value: String)
inline class HtmlContent(val value: String)
inline class HtmlWebPageContent(val value: String)

data class RenderedWebPage(
    val coordinates: Coordinates.Endpoint,
    val content: HtmlWebPageContent
)

data class BlogVM(
    val title: String = "",
    val description: String = "",
    val footer: FooterVM = FooterVM(),
    val navigation: List<NavItemVM> = emptyList(),
    val githubUser: String = ""
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
