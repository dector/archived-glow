package io.github.dector.glow.core


inline class MarkdownContent(val value: String)
inline class HtmlContent(val value: String)
inline class HtmlWebPageContent(val value: String)
inline class WebPagePath(val value: String)

data class WebPage(
        val path: WebPagePath,
        val content: HtmlWebPageContent
)