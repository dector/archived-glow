package io.github.dector.glow.core

import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec

class PathUtilsKtTest : BehaviorSpec({

    Given("path simplifier") {
        val cases = mapOf(
            "with one space" to "with-one-space",
            "with    few   spaces" to "with-few-spaces",
            "with-dash" to "with-dash",
            "  not trimmed  " to "not-trimmed",
            "title!@#$%^&*()" to "title",
            "/some/path" to "some-path",
            "with digits 123 456 789 0" to "with-digits-123-456-789-0"
        )

        cases.forEach { (sourceString, expectedString) ->
            When("source string is '$sourceString'") {
                val result = sourceString.simplifyForWebPath()

                Then("simplified string is '$expectedString'") {
                    result shouldBe expectedString
                }
            }
        }
    }
})
