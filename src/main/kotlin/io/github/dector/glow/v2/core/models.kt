package io.github.dector.glow.v2.core

import io.github.dector.glow.v2.mockimpl.NotePath
import io.github.dector.glow.v2.mockimpl.PagePath
import java.io.File
import java.time.Instant

@Deprecated("")
data class BlogData(val posts: List<Post>)

@Deprecated("")
data class ProcessedData(val indexPages: List<ProcessedPage>,
                         val postPages: List<ProcessedPage>,
                         val tagPages: List<ProcessedPage>)

@Deprecated("")
sealed class PublishResult {
    class Success : PublishResult()
    class Fail : PublishResult()
}

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

data class MetaInfo(    // Config?
        val pages: List<PageInfo>,
        val notes: List<NoteInfo>
)

data class PageInfo(
        val id: String,
        val title: String,
        val sourceFile: File
)

data class NoteInfo(
        val id: String,
        val title: String,
        val isDraft: Boolean,
        val createdAt: Instant,
        val sourceFile: File
)

data class Page(
        val info: PageInfo,
        val markdownContent: String
)

inline class MarkdownContent(val value: String)
inline class HtmlContent(val value: String)
inline class HtmlWebPageContent(val value: String)
inline class WebPagePath(val value: String)

data class Page2(
        val title: String,
        val createdAt: Instant?,
        val sourceFile: File,
        val content: MarkdownContent
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

data class Note(
        val info: NoteInfo,
        val markdownContent: String
)

data class RenderedPage(
        val path: PagePath,
        val content: String
)

data class RenderedNote(
        val path: NotePath,
        val info: NoteInfo,
        val content: String
)