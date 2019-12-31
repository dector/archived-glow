package io.github.dector.glow.plugins.notes

import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec

class MarkdownUtilsTest : BehaviorSpec({

    Given("Mardown content") {
        val cases = listOf(
            Case("pefect case", Headers.perfect, Content.perfect, Expectation.perfect),
            Case("header without content", Headers.perfect, Content.empty, Expectation.emptyContent),
            Case("content without header", Headers.empty, Content.withSpacing, Expectation.emptyHeader),
            Case("header and content with spacing", Headers.withSpacing, Content.withSpacing, Expectation.withSpacing)
        )

        cases.forEach { case ->
            When(case.name) {
                Then("expect parsed result") {
                    parseMarkdownFrom(case.header + "\n" + case.content) shouldBe case.expectation
                }
            }
        }
    }
})

private object Headers {
    val perfect =
        """
            ---
            header
            ---
        """.trimIndent()

    val empty = "\n\n\n"

    val withSpacing =
        """
            |
            |
            |---
            |header with spacing
            |---
            |
        """.trimMargin()
}

private object Content {
    val perfect =
        """
            perfect
            content
        """.trimIndent()

    val empty = "\n\n\n"

    val withSpacing =
        """
            |
            |
            |
            |
            |content with spacing
            |
            |
        """.trimMargin()
}

private object Expectation {
    val perfect = ParsedResult("header", "perfect\ncontent")
    val emptyContent = ParsedResult("header", "")
    val emptyHeader = ParsedResult(content = "content with spacing")
    val withSpacing = ParsedResult("header with spacing", "content with spacing")
}

data class Case(
    val name: String,
    val header: String,
    val content: String,
    val expectation: ParsedResult
)
