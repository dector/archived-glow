package io.github.dector.glow.templates.camellia

import io.github.dector.glow.core.BlogVm
import io.github.dector.glow.core.NoteVm
import io.github.dector.glow.core.templates.TemplateEngine

class CamelliaEngine : TemplateEngine {

    override fun notesIndex(blog: BlogVm, notes: List<NoteVm>) = webPage(blog) {
        title = "Notes"

        /*content {
            todoPlaceholder()
        }*/
    }
}
