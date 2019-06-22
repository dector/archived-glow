package io.github.dector.glow.plugins.notes

import io.github.dector.glow.core.Empty
import io.github.dector.glow.core.WebPagePath
import io.github.dector.glow.core.config.Config
import io.github.dector.glow.core.simplifyForWebPath
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


interface NotesPathResolver {

    fun resolve(note: Note2, buildUrlPath: Boolean = false): WebPagePath
    fun resolveNotesIndex(): WebPagePath
    fun resolveNotesArchive(): WebPagePath
}

class NotesWebPathResolver(
    private val config: Config
) : NotesPathResolver {

    private val notePathDateFormatter = DateTimeFormatter
        .ofPattern("uuuu/MM/dd")
        .withZone(ZoneOffset.UTC)

    override fun resolve(note: Note2, buildUrlPath: Boolean): WebPagePath {
        note.publishedAt ?: return WebPagePath.Empty

        val dir = notePathDateFormatter.format(note.publishedAt)
        val instancePath = note.title.simplifyForWebPath()

        val path = "${config.old.output.notesPath}/$dir/$instancePath/"
        return if (buildUrlPath)
            WebPagePath(path)
        else indexWebPath(path)
    }

    override fun resolveNotesIndex(): WebPagePath =
        indexWebPath(config.old.output.notesPath)

    override fun resolveNotesArchive(): WebPagePath =
        indexWebPath("${config.old.output.notesPath}/archive")
}

private fun indexWebPath(path: String) = WebPagePath(
    buildString {
        append(path)

        if (!path.endsWith("/")) append("/")

        append("index.html")
    }
)
