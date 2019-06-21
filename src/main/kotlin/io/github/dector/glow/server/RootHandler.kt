package io.github.dector.glow.server

import io.github.dector.glow.core.WebPage
import io.javalin.http.Context
import io.javalin.http.Handler

class RootHandler(private val storage: Collection<WebPage>) : Handler {

    override fun handle(ctx: Context) {
        val path = ctx.path()

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
}
