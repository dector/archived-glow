package io.github.dector.glow.v2.dumbimpl

import io.github.dector.glow.v2.core.DataPublisher
import io.github.dector.glow.v2.core.PublishResult
import io.github.dector.glow.v2.models.ProcessedPage
import java.io.File


val dumbDataPublisher: DataPublisher = { data ->
    val buildDir = File("v2/out").apply {
        deleteRecursively()
        mkdir()
    }

    fun writeToFile(page: ProcessedPage) {
        val filename = "${page.path}.html"
        val file = File(buildDir, filename)
        file.parentFile.mkdirs()

        file.writeText(page.content)
    }

    data.pages.forEach (::writeToFile)
    data.indexPages.forEach (::writeToFile)
    data.tagPages.forEach (::writeToFile)

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