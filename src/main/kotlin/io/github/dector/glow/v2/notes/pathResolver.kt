package io.github.dector.glow.v2.notes

import io.github.dector.glow.v2.core.Note2
import io.github.dector.glow.v2.core.WebPagePath
import io.github.dector.glow.v2.implementation.ProjectConfig
import io.github.dector.glow.v2.implementation.simplifyForWebPath
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


interface NotesPathResolver {

    fun resolve(note: Note2): WebPagePath
    fun resolveNotesIndex(): WebPagePath
    fun resolveNotesArchive(): WebPagePath
}

class NotesWebPathResolver(
        private val config: ProjectConfig
) : NotesPathResolver {

    private val notePathDateFormatter = DateTimeFormatter.ofPattern("uuuu/MM/dd")
            .withZone(ZoneOffset.UTC)

    override fun resolve(note: Note2): WebPagePath {
        val dir = if (note.publishedAt != null) {
            notePathDateFormatter.format(note.publishedAt)
        } else {
            "lost"
        }
        val instancePath = "${note.title.simplifyForWebPath()}.html"

        return WebPagePath(config.output.notesPath + "/" + dir + "/" + instancePath)
    }

    override fun resolveNotesIndex(): WebPagePath {
        return WebPagePath(config.output.notesPath + "/index.html")
    }

    override fun resolveNotesArchive(): WebPagePath {
        return WebPagePath(config.output.notesPath + "/archive/index.html")
    }
}