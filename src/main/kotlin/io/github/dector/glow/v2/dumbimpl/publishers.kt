package io.github.dector.glow.v2.dumbimpl

import io.github.dector.glow.v2.DataPublisher
import io.github.dector.glow.v2.PublishResult
import java.io.File


val dumbDataPublisher: DataPublisher = { data ->
    val buildDir = File("v2/out").apply {
        deleteRecursively()
        mkdir()
    }

    data.pages.forEach { page ->
        val filename = "${page.path}.html"
        val file = File(buildDir, filename)

        file.writeText(page.content)
    }

    /*

    // Index posts
    block("INDEX PAGES total: ${data.indexPages.size}") {
        data.indexPages.forEach {
            page("--- --- PAGE ${it.pageNumber}/${it.totalPages} --- ---") {
                println(it.articles.joinToString(separator = "\n\n") {
                    """ |${it.title}
                        |${it.content}
                        |${it.tags.joinToString(prefix = "[", postfix = "]")}
                    """.trimMargin()
                })
                println()
                println("<< Prev: ${it.prevPage?.pageNumber}")
                println("Next: ${it.nextPage?.pageNumber} >>")
            }
        }
    }

    // Pages
    block("PAGES total: ${data.posts.size}") {
        data.posts.forEach {
            page("--- --- --- --- --- ---") {
                println("""
                        |${it.title}
                        |${it.content}
                        |${it.tags.joinToString(prefix = "[", postfix = "]")}
                        |<< Prev: ${it.prevPage?.title}
                        |Next: ${it.nextPage?.title} >>
                    """.trimMargin()
                )
            }
        }
    }

    // Tag posts
    block("TAG PAGES total: ${data.tagPages.size}") {
        data.tagPages.forEach {
            page("--- --- TAG: ${it.tag} --- ---") {
                println(it.articles.joinToString(separator = "\n\n") {
                    """ |${it.title}
                        |${it.content}
                        |${it.tags.joinToString(prefix = "[", postfix = "]")}
                    """.trimMargin()
                })
            }
        }
    }
*/
    PublishResult.Success()
}