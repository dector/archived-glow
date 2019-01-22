package io.github.dector.glow.v2.core

import io.github.dector.glow.v2.mockimpl.PagePath
import io.github.dector.glow.v2.mockimpl.ProjectConfig


interface PathResolver {

    fun resolveForPage(info: PageInfo): PagePath
}

class WebPathResolver(private val config: ProjectConfig) : PathResolver {

    override fun resolveForPage(info: PageInfo) = run {
        val instancePath = if (isIndexPage(info)) "index.html"
        else "${info.id}.html"

        PagePath(
                parentPath = "/",
                instancePath = instancePath
        )
    }

    private fun isIndexPage(info: PageInfo) = info.id == "index"
}