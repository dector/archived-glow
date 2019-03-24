package io.github.dector.glow.v2.core

import java.io.File
import java.time.Instant

@Deprecated("")
data class BlogData(val posts: List<Post>)

@Deprecated("")
data class ProcessedData(val indexPages: List<ProcessedPage>,
                         val postPages: List<ProcessedPage>,
                         val tagPages: List<ProcessedPage>)

class GlowExecutionResult

@Deprecated("")
data class Post(
        val title: String,
        val content: String,
        val tags: List<String> = emptyList(),
        val isDraft: Boolean = false)

@Deprecated("")
data class ProcessedPage(
        val path: String,   // FIXME remove path from processed model. Should be handled in Publisher instead
        val content: String)

data class PageInfo(
        val id: String,
        val title: String,
        val sourceFile: File,
        val isSection: Boolean
)

data class NoteInfo(
        val id: String,
        val title: String,
        val isDraft: Boolean,
        val createdAt: Instant,
        val sourceFile: File
)

inline class MarkdownContent(val value: String)
inline class HtmlContent(val value: String)
inline class HtmlWebPageContent(val value: String)
inline class WebPagePath(val value: String)

data class Page2(
        val title: String,
        val createdAt: Instant?,
        val sourceFile: File,
        val content: MarkdownContent,
        val isSection: Boolean
)

data class Page2VM(
        val title: String,
        val createdAt: Instant?,
        val path: WebPagePath,
        val content: HtmlContent
)

data class WebPage(
        val path: WebPagePath,
        val content: HtmlWebPageContent
)

data class Note2(
        val title: String,
        val createdAt: Instant?,
        val publishedAt: Instant?,
        val isDraft: Boolean,
        val sourceFile: File,
        val content: MarkdownContent
)

data class Note2VM(
        val title: String,
        val createdAt: Instant?,
        val publishedAt: Instant?,
        val path: WebPagePath,
        val content: HtmlContent,
        val previewContent: HtmlContent
)