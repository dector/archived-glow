package io.github.dector.glow.v2.core

data class BlogData(val posts: List<Post>)
data class ProcessedData(val indexPages: List<ProcessedPage>,
                         val postPages: List<ProcessedPage>,
                         val tagPages: List<ProcessedPage>)

sealed class PublishResult {
    class Success : PublishResult()
    class Fail : PublishResult()
}

data class Result(val publishResult: PublishResult)

data class Post(
        val title: String,
        val content: String,
        val tags: List<String> = emptyList(),
        val isDraft: Boolean = false)

data class ProcessedPage(
        val path: String,   // FIXME remove path from processed model. Should be handled in Publisher instead
        val content: String)