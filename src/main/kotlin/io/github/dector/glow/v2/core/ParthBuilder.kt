package io.github.dector.glow.v2.core

import io.github.dector.glow.v2.mockimpl.ProjectConfig
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


interface PathResolver {

    fun resolve(page: Page2): WebPagePath
    fun resolve(note: Note2): WebPagePath
    fun resolveNotesIndex(): WebPagePath
    fun resolveNotesArchive(): WebPagePath
}

class WebPathResolver(private val config: ProjectConfig) : PathResolver {
    private val notePathDateFormatter = DateTimeFormatter.ofPattern("uuuu/MM/dd")
            .withZone(ZoneOffset.UTC)

    override fun resolve(page: Page2): WebPagePath {
        fun pageInstanceName() = page.title.simplify()

        val instancePath = when {
            page.sourceFile.nameWithoutExtension == "index" -> "index.html"
            page.isSection -> "${pageInstanceName()}/index.html"
            else -> "${pageInstanceName()}.html"
        }

        return WebPagePath(instancePath)
    }

    override fun resolve(note: Note2): WebPagePath {
        val dir = if (note.publishedAt != null) {
            notePathDateFormatter.format(note.publishedAt)
        } else {
            "lost"
        }
        val instancePath = "${note.title.simplify()}.html"

        return WebPagePath(config.output.notesPath + "/" + dir + "/" + instancePath)
    }

    override fun resolveNotesIndex(): WebPagePath {
        return WebPagePath(config.output.notesPath + "/index.html")
    }

    override fun resolveNotesArchive(): WebPagePath {
        return WebPagePath(config.output.notesPath + "/archive/index.html")
    }

    private fun String.simplify() = this
            .replace(" ", "-")
            .replace(Regex("[^\\w_-]"), "")
            .toLowerCase()
}