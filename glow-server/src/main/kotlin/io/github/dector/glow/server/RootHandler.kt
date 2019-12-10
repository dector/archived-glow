package io.github.dector.glow.server

import io.github.dector.glow.core.WebPage
import io.github.dector.glow.server.RequestedResource.StaticResource
import io.javalin.http.Context
import io.javalin.http.Handler
import org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404
import org.eclipse.jetty.http.HttpStatus.OK_200
import java.io.File

@Suppress("MoveVariableDeclarationIntoWhen")
class RootHandler(private val storage: Collection<WebPage>) : Handler {

    override fun handle(ctx: Context) {
        val path = ctx.path()

        logRequest(path)

        val resource = detectRequestedResource(path)
        when (resource) {
            is StaticResource -> respondFor(ctx, resource)
            is RequestedResource.Page -> respondFor(ctx, resource)
        }
    }

    private fun respondFor(ctx: Context, resource: StaticResource) {
        val resourcePath = resource.relativePath
        val file = File("templates-hyde/src/main/res/$resourcePath")

        if (!file.exists()) {
            ctx.status(NOT_FOUND_404).result("Static resource not found")
            return
        }

        val extension = resourcePath.substringAfterLast('.', "")
        val contentType = when (extension) {
            "css" -> "text/css"
            "ico" -> "image/x-icon"
            else -> ""
        }

        if (contentType.isNotEmpty()) ctx.contentType(contentType)
        ctx.status(OK_200).result(file.inputStream())
    }

    private fun respondFor(ctx: Context, resource: RequestedResource.Page) {
        val page = findPageFor(resource.fullPath)

        page ?: run {
            ctx.status(NOT_FOUND_404).result("Page not found")
            return
        }

        ctx.status(OK_200)
        if (resource.fullPath.endsWith(".xml")) {
            ctx.contentType("text/xml")
        } else {
            ctx.contentType("text/html")
        }
        ctx.result(page.content.value)
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

sealed class RequestedResource {
    data class StaticResource(val relativePath: String) : RequestedResource()
    data class Page(val fullPath: String) : RequestedResource()
}

private fun detectRequestedResource(path: String): RequestedResource = when {
    path.startsWith("/public/") ->
        StaticResource(path.removePrefix("/public/"))
    else ->
        RequestedResource.Page(path)
}
