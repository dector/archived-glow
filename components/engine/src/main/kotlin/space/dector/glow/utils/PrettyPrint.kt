package space.dector.glow.utils

import org.w3c.tidy.Tidy
import space.dector.glow.engine.HtmlWebPageContent
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
