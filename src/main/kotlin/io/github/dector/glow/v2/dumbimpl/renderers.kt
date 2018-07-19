package io.github.dector.glow.v2.dumbimpl

import io.github.dector.glow.v2.core.Post
import io.github.dector.glow.v2.dumbimpl.utils.htmlPage
import io.github.dector.glow.v2.dumbimpl.utils.isLast
import kotlinx.html.*


typealias Tag = String

typealias IndexPagesRenderer = (PaginatedPage) -> String
typealias PostPageRenderer = (Post) -> String
typealias TagPageRender = (Tag, PaginatedPage) -> String

val indexPagesRenderer: IndexPagesRenderer = { info ->
    htmlPage("${info.pageNumber} / ${info.totalPages}") {
        info.posts.forEach { post ->
            renderPost(post)

            if (!info.posts.isLast(post)) br
        }

        renderPaging(info)
    }
}

val postPageRenderer: PostPageRenderer = { post ->
    htmlPage(post.title) {
        renderPost(post)
    }
}

val tagPageRenderer: TagPageRender = { tag, info ->
    htmlPage("[$tag] :: ${info.pageNumber} / ${info.totalPages}") {
        +tag; hr { }

        info.posts.forEach { post ->
            renderPost(post)

            if (!info.posts.isLast(post)) br
        }

        renderPaging(info)
    }
}

private fun BODY.renderPost(post: Post) {
    h2 {
        a(href = postPagePathResolver(post)) { +post.title }
    }

    unsafe { +post.content }

    post.tags.takeIf { it.isNotEmpty() }?.let { tags ->
        p {
            +"["

            tags.forEach { tag ->
                a(href = "/tags/$tag") { +tag }

                if (!tags.isLast(tag)) +", "
            }

            +"]"
        }
    }
}

private fun BODY.renderPaging(info: PaginatedPage) {
    if (info.prevPagePath.isNotEmpty()) {
        br; br
        a(href = info.prevPagePath) { +"<< Previous" }
    }

    if (info.nextPagePath.isNotEmpty()) {
        br; br
        a(href = info.prevPagePath) { +"Next >>" }
    }
}