package io.github.dector.glow.v2.core

import io.github.dector.glow.v2.models.Post
import io.github.dector.glow.v2.models.ProcessedPage

data class BlogData(val posts: List<Post>)
data class ProcessedData(val indexPages: List<ProcessedPage>,
                         val pages: List<ProcessedPage>,
                         val tagPages: List<ProcessedPage>)

sealed class PublishResult {
    class Success : PublishResult()
    class Fail : PublishResult()
}

data class Result(val publishResult: PublishResult)