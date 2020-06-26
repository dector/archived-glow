package io.github.dector.glow.plugins.notes

import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.core.WebPagePath
import io.github.dector.glow.core.path.cleanupTitleForWebPath
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


interface NotesPathResolver {

    fun resolve(note: Note, buildUrlPath: Boolean = false): WebPagePath
    fun resolveNotesIndex(): WebPagePath
    fun resolveNotesArchive(): WebPagePath

    fun resolveTagPage(tag: String): WebPagePath
}

// TODO test
class NotesWebPathResolver(
    config: RuntimeConfig
) : NotesPathResolver {

    private val notesPath = config.glow.notes.destinationPath

    private val notePathDateFormatter = DateTimeFormatter
        .ofPattern("uuuu/MM/dd")
        .withZone(ZoneOffset.UTC)

    override fun resolve(note: Note, buildUrlPath: Boolean): WebPagePath {
        val dir = if (note.publishedAt != null)
            notePathDateFormatter.format(note.publishedAt)
        else DraftsDirName
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

    override fun resolveTagPage(tag: String): WebPagePath =
        indexWebPath("$notesPath/tags/$tag")

    private companion object {
        const val DraftsDirName = "drafts"
    }
}

private fun indexWebPath(path: String) = WebPagePath(
    buildString {
        append(path)

        if (!path.endsWith("/")) append("/")

        append("index.html")
    }
)
