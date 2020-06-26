package io.github.dector.glow.utils

import io.github.dector.glow.engine.HtmlWebPageContent
import org.w3c.tidy.Tidy
import java.io.StringWriter

fun HtmlWebPageContent.prettyPrint(): HtmlWebPageContent {
    val tidy = Tidy().apply {
        quiet()

        dropProprietaryTags = false
        fixComments = false
        tidyMark = false

        wraplen = 120

        smartIndent = true
    }

    val outputString = StringWriter()
    tidy.parse(value.reader(), outputString)

    return HtmlWebPageContent(outputString.toString())
}

private fun Tidy.quiet() = apply {
    quiet = true
    showWarnings = false
    showErrors = 0
}
