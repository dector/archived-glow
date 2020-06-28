package io.github.dector.glow.server

import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.coordinates.inHostPath
import io.github.dector.glow.engine.RenderedWebPage
import io.github.dector.glow.server.RequestedResource.StaticResource
import io.github.dector.glow.utils.ThemeResourcesPath
import io.javalin.http.Context
import io.javalin.http.Handler
import org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404
import org.eclipse.jetty.http.HttpStatus.OK_200
import java.io.File
import java.nio.file.Paths

@Suppress("MoveVariableDeclarationIntoWhen")
class RootHandler(
    private val config: RuntimeConfig,
    private val storage: Collection<RenderedWebPage>
) : Handler {

    override fun handle(ctx: Context) {
        // TODO decode url
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
        val file = File("$ThemeResourcesPath/$resourcePath")
            // FIXME hack
            .let {
                if (!it.exists())
                    config.glow.sourceDir.resolve("assets/").resolve(resourcePath).toFile()
                else it
            }

        if (!file.exists()) {
            ctx.status(NOT_FOUND_404).result("Static resource not found")
            return
        }

        val extension = resourcePath.substringAfterLast('.', "")
        val contentType = when (extension) {
            "css" -> "text/css"
            "ico" -> "image/x-icon"
            "png" -> "image/png"
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

    private fun findPageFor(path: String): RenderedWebPage? {
        val requestedPath = Paths.get("/")
            .resolve(Paths.get(path))
            .normalize()
            .let { if (it.fileName?.toString() == "index.html") it.parent else it }
            .toString()
            .let { if (it != "/") "$it/" else "/" }

        return storage.find { it.coordinates.inHostPath() == requestedPath }
    }

    private fun detectRequestedResource(path: String): RequestedResource = when {
        path.startsWith(config.glow.assets.destinationPath.toString()) ->
            StaticResource(config.glow.assets.destinationPath.relativize(Paths.get(path)).toString())
        else ->
            RequestedResource.Page(path)
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
