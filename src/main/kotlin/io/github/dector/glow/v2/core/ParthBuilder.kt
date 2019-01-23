package io.github.dector.glow.v2.core

import io.github.dector.glow.v2.mockimpl.NotePath
import io.github.dector.glow.v2.mockimpl.PagePath
import io.github.dector.glow.v2.mockimpl.ProjectConfig


interface PathResolver {

    fun resolveForPage(info: PageInfo): PagePath
    fun resolveForNote(it: NoteInfo): NotePath
    fun notesIndex(): PagePath
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

    override fun resolveForNote(it: NoteInfo): NotePath {
        return NotePath(
                path = config.output.notesPath + "/${it.id}.html"
        )
    }

    override fun notesIndex() = PagePath(
            parentPath = config.output.notesPath,
            instancePath = "/"
    )

    private fun isIndexPage(info: PageInfo) = info.id == "index"
}