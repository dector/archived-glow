package space.dector.glow.templates.hyde

import com.mitchellbosecke.pebble.PebbleEngine
import space.dector.glow.config.RuntimeConfig
import space.dector.glow.di.DI
import space.dector.glow.di.get
import space.dector.glow.engine.HtmlWebPageContent
import space.dector.glow.engine.RenderContext
import space.dector.glow.plugins.notes.NoteVM
import space.dector.glow.theming.Template
import java.io.StringWriter
import java.nio.file.Path

class DasLightTemplate : Template {

    private val pebble = PebbleEngine.Builder()
        .build()
        .apply { loader.setPrefix("components/template-das-light/src/main/resources/templates/") }

    override fun notesIndexPage(notes: List<NoteVM>, context: RenderContext): HtmlWebPageContent {
        val pageVm = PageVM(
            title = context.blog.title
        )

        return HtmlWebPageContent(
            pebble.render("notesIndex", mapOf(
                "page" to pageVm,
                "notes" to notes
            ))
        )
    }

    override fun note(note: NoteVM, context: RenderContext): HtmlWebPageContent {
        val pageVm = PageVM(
            title = context.blog.title + " | " + note.title
        )

        return HtmlWebPageContent(
            pebble.render("notePage", mapOf(
                "page" to pageVm,
                "note" to note
            ))
        )
    }

    override fun notesArchive(notes: List<NoteVM>, context: RenderContext) = HtmlWebPageContent("")
//        webPage(context) {
//            notesIndexContent(notes, title = "Archive", context = context, displayFullNotes = true)
//        }

    override fun tagPage(notes: List<NoteVM>, tag: String, context: RenderContext) = HtmlWebPageContent("")
//        webPage(context.blog, context.currentNavSection, "#$tag") {
//            tagPageContent(notes, tag, context)
//        }
}

internal fun assetPath(path: String, dirPath: Path): String =
    dirPath.resolve(path).toString()

private fun assetPath(path: String, config: RuntimeConfig = DI.get()): String =
    assetPath(path, config.glow.assets.destinationPath)

internal fun PebbleEngine.render(
    template: String,
    vm: Map<String, Any?> = emptyMap()
): String =
    StringWriter().apply {
        getTemplate("$template.peb").evaluate(this, vm)
    }.toString()

data class PageVM(
    val title: String
)
