package io.github.dector.glow.plugins.notes

import io.github.dector.glow.config.RuntimeConfig
import io.github.dector.glow.coordinates.Coordinates
import io.github.dector.glow.engine.WebPagePath
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


interface NotesPathResolver {

    fun coordinatesFor(note: Note): Coordinates.Endpoint

    @Deprecated("")
    fun resolve(note: Note, buildUrlPath: Boolean = false): WebPagePath

    @Deprecated("")
    fun resolveNotesPage(pageNum: Int): WebPagePath

    @Deprecated("")
    fun resolveNotesArchive(): WebPagePath

    @Deprecated("")
    fun resolveTagPage(tag: String): WebPagePath
}

// TODO test
class NotesWebPathResolver(
    config: RuntimeConfig
) : NotesPathResolver {

    @Deprecated("Use `sectionPath`")
    private val notesPath = config.glow.notes.destinationPath

    private val sectionPath = config.glow.notes
        .destinationPath
        .trim('/')

    private val notePathDateFormatter = DateTimeFormatter
        .ofPattern("uuuu/MM/dd")
        .withZone(ZoneOffset.UTC)

    override fun coordinatesFor(note: Note): Coordinates.Endpoint {
        val dir = if (note.publishedAt != null)
            notePathDateFormatter.format(note.publishedAt)
        else DraftsDirName
        val instance = note.title.cleanupTitleForWebPath()

        return Coordinates.Endpoint(
            section = sectionPath,
            inner = dir,
            name = instance
        )
    }

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

    override fun resolveNotesPage(pageNum: Int): WebPagePath =
        pageWebPath(notesPath) { if (pageNum != 1) "page$pageNum/index.html" else "index.html" }

    override fun resolveNotesArchive(): WebPagePath =
        indexWebPath("$notesPath/archive")

    override fun resolveTagPage(tag: String): WebPagePath =
        indexWebPath("$notesPath/tags/$tag")

    private companion object {
        const val DraftsDirName = "drafts"
    }
}

private fun indexWebPath(path: String): WebPagePath = pageWebPath(path) { "index.html" }

private fun pageWebPath(path: String, nameBuilder: () -> String) = WebPagePath(
    buildString {
        append(path)

        if (!path.endsWith("/")) append("/")

        append(nameBuilder())
    }
)
