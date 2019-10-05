package io.github.dector.glow.templates.camellia

import io.github.dector.glow.core.BlogVm
import io.github.dector.glow.core.NoteVm
import io.github.dector.glow.core.templates.TemplateEngine
import io.kotlintest.specs.BehaviorSpec

class CamelliaEngineTest : BehaviorSpec({

    Given("Template Engine") {
        val engine: TemplateEngine = CamelliaEngine()

        And("Blog with few notes") {
            val blog = BlogVm(
                title = "My Blog"
            )
            val notes = listOf(
                NoteVm(
                    title = "First"
                ),
                NoteVm(
                    title = "Second"
                )
            )

            When("Rendering notes index") {
                val content = engine.notesIndex(blog, notes)

                Then("Content should be valid") {
                    //                    content shouldBe HtmlContent("""
//                        |
//                    """.trimMargin())
                }
            }
        }

    }
})
