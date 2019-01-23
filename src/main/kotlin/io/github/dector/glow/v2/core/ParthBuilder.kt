package io.github.dector.glow.v2.core

import io.github.dector.glow.v2.mockimpl.ProjectConfig
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


interface PathResolver {

    fun resolve(page: Page2): WebPagePath
    fun resolve(note: Note2): WebPagePath
}

class WebPathResolver(private val config: ProjectConfig) : PathResolver {

    private val notePathDateFormatter = DateTimeFormatter.ofPattern("uuuu/MM/dd")
            .withZone(ZoneOffset.UTC)

    override fun resolve(page: Page2): WebPagePath {
        val instancePath = if (page.sourceFile.nameWithoutExtension == "index") "index.html"
        else "${page.sourceFile.nameWithoutExtension}.html"

        return WebPagePath(instancePath)
    }

    override fun resolve(note: Note2): WebPagePath {
        val dir = if (note.publishedAt != null) {
            notePathDateFormatter.format(note.publishedAt)
        } else {
            "lost"
        }
        val instancePath = "${note.sourceFile.nameWithoutExtension}.html"

        return WebPagePath(config.output.notesPath + "/" + dir + "/" + instancePath)
    }

    /*override fun resolveForPage(info: PageInfo) = run {
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
    )*/

//    private fun isIndexPage(info: PageInfo) = info.id == "index"
}