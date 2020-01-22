package io.github.dector.glow.plugins.notes

import io.github.dector.glow.core.Empty
import io.github.dector.glow.core.WebPagePath
import io.github.dector.glow.core.config.ProjectConfig
import io.github.dector.glow.core.path.cleanupTitleForWebPath
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


interface NotesPathResolver {

    fun resolve(note: Note, buildUrlPath: Boolean = false): WebPagePath
    fun resolveNotesIndex(): WebPagePath
    fun resolveNotesArchive(): WebPagePath
}

class NotesWebPathResolver(
    config: ProjectConfig
) : NotesPathResolver {

    private val notesPath = config.plugins.notes.path

    private val notePathDateFormatter = DateTimeFormatter
        .ofPattern("uuuu/MM/dd")
        .withZone(ZoneOffset.UTC)

    override fun resolve(note: Note, buildUrlPath: Boolean): WebPagePath {
        note.publishedAt ?: return WebPagePath.Empty

        val dir = notePathDateFormatter.format(note.publishedAt)
        val instancePath = note.title.cleanupTitleForWebPath()

        val path = "${notesPath}/$dir/$instancePath/"
        return if (buildUrlPath)
            WebPagePath(path)
        else indexWebPath(path)
    }

    override fun resolveNotesIndex(): WebPagePath =
        indexWebPath(notesPath)

    override fun resolveNotesArchive(): WebPagePath =
        indexWebPath("$notesPath/archive")
}

private fun indexWebPath(path: String) = WebPagePath(
    buildString {
        append(path)

        if (!path.endsWith("/")) append("/")

        append("index.html")
    }
)
