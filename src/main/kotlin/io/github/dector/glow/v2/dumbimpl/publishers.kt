package io.github.dector.glow.v2.dumbimpl

import io.github.dector.glow.v2.DataPublisher
import io.github.dector.glow.v2.PublishResult


val dumbDataPublisher: DataPublisher = { data ->
    fun block(message: String, body: () -> Unit) {
        println("=== === === === === === === === === ===")
        println(message)
        println()
        body()
        println("=== === === === === === === === === ===")
        println()
        println()
    }

    fun page(message: String, body: () -> Unit) {
        println(message)
        body()
        println(message)
        println()
    }

    // Index pages
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
    block("PAGES total: ${data.pages.size}") {
        data.pages.forEach {
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

    // Tag pages
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

    PublishResult.Success()
}