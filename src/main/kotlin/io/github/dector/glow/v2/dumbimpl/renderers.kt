package io.github.dector.glow.v2.dumbimpl

import io.github.dector.glow.v2.core.Post


typealias Tag = String

typealias IndexPagesRenderer = (PaginatedPage) -> String
typealias PostPageRenderer = (Post) -> String
typealias TagPageRender = (Tag, PaginatedPage) -> String

val indexPagesRenderer: IndexPagesRenderer = { info ->
    html(title = "$info.pageNumber / $info.totalPages") {
        info.posts.joinToString(separator = "\n<hr/>\n") {
            renderPostPart(it)
        } + (
                (if (info.prevPagePath.isNotEmpty()) "<br/><br/><a href='${info.prevPagePath}'><< Previous</a>" else "") +
                        (if (info.nextPagePath.isNotEmpty()) "<br/><br/><a href='${info.nextPagePath}'>Next >></a>" else "")
                )
    }
}

val postPageRenderer: PostPageRenderer = { post ->
    html(post.title) { renderPostPart(post) }
}

val tagPageRenderer: TagPageRender = { tag, info ->
    html(title = "[$tag] :: ${info.pageNumber} / ${info.totalPages}") {
        info.posts.joinToString(separator = "\n<hr/>\n", prefix = "<h1>$tag</h1>") {
            "<h2>${it.title}</h1><br/>${it.content}"
        } + (
                (if (info.prevPagePath.isNotEmpty()) "<br/><br/><a href='${info.prevPagePath}'><< Previous</a>" else "") +
                        (if (info.nextPagePath.isNotEmpty()) "<br/><br/><a href='${info.nextPagePath}'>Next >></a>" else "")
                )
    }
}

private fun renderPostPart(post: Post): String {
    return "<h2><a href='${postPagePathResolver(post)}'>${post.title}</a></h2><br/>${post.content}" +
            """
                | [${post.tags.joinToString { "<a href='/tags/$it/'>$it</a>" }}]
                """.trimMargin()
}