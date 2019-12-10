package io.github.dector.glow.server

import io.github.dector.glow.core.WebPage
import io.javalin.http.Context
import io.javalin.http.Handler
import java.io.File

class RootHandler(private val storage: Collection<WebPage>) : Handler {

    override fun handle(ctx: Context) {
        val path = ctx.path()

        logRequest(path)

        // FIXME serve static resourses better
        if (path.startsWith("/public/")) {
            val resourcePath = path.removePrefix("/public/")

            val file = File("templates-hyde/src/main/res/$resourcePath")
            if (file.exists()) {
                when {
                    resourcePath.endsWith(".css") ->
                        ctx.contentType("text/css")
                    resourcePath.endsWith(".ico") ->
                        ctx.contentType("image/x-icon")
                }
                ctx.status(200).result(file.readText())
            } else {
                ctx.status(404).result("Resource not found")
            }
            return
        }

        val page = findPageFor(path)

        if (page != null) {
            ctx.html(page.content.value)
        } else {
            ctx.status(404).result("Not found")
        }
    }

    private fun findPageFor(path: String): WebPage? {
        val exactPage = storage.find { it.path.value == path }
        if (exactPage != null) return exactPage

        val clearedPath = path.trimEnd('/')
        val pathToIndex = "$clearedPath/index.html"
        return storage.find { it.path.value == pathToIndex }
    }

    @Suppress("ConstantConditionIf")
    private fun logRequest(path: String) {
        if (!LOG_REQUESTS) return

        println("GET: $path")
    }

    companion object {

        private const val LOG_REQUESTS = true
    }
}
