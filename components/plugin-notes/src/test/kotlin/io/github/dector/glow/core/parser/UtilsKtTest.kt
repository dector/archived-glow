package io.github.dector.glow.core.parser

import io.github.dector.glow.plugins.notes.parseCreatedAt
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class UtilsKtTest : BehaviorSpec({

    Given("invalid date strings") {
        val cases = listOf(
            null,
            "32-06-19"
        )

        cases.forEach { dateString ->
            When("date string is $dateString") {
                val result = parseCreatedAt(dateString)

                Then("result is null") {
                    result.shouldBeNull()
                }
            }
        }
    }

    Given("valid date strings") {
        val cases = listOf(
            "9-06-2019",
            "9-06-19",
            "9-6-19",
            "9-06-2019",
            "09-06-2019"
        )
        val expectedDate = date(9, 6, 2019)

        cases.forEach { dateString ->
            When("date string is $dateString") {
                val result = parseCreatedAt(dateString)

                Then("result is 9/6/2019") {
                    result shouldBe expectedDate
                }
            }
        }
    }
})

private fun date(day: Int, month: Int, year: Int): Instant =
    LocalDate.of(year, month, day)
        .atStartOfDay()
        .toInstant(ZoneOffset.UTC)
