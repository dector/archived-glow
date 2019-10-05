package io.github.dector.glow.core.templates

import io.github.dector.glow.core.BlogVm
import io.github.dector.glow.core.HtmlPage
import io.github.dector.glow.core.NoteVm

interface TemplateEngine {

    fun notesIndex(blog: BlogVm, notes: List<NoteVm>): HtmlPage
}
