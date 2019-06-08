package io.github.dector.glow.core.parser

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class UtilsKtTest {

    @Test
    @DisplayName("parseCreatedAt() with invalid string")
    fun `parseCreatedAt invalid`() {
        assertThat(parseCreatedAt(null))
                .isNull()
        assertThat(parseCreatedAt(null))
                .isNull()
        assertThat(parseCreatedAt("32-06-19"))
                .isNull()
    }

    @Test
    @DisplayName("parseCreatedAt() with valid string")
    fun `parseCreatedAt valid`() {
        val expectedDate = date(9, 6, 2019)

        assertThat(parseCreatedAt("9-06-2019"))
                .isEqualTo(expectedDate)
        assertThat(parseCreatedAt("9-06-19"))
                .isEqualTo(expectedDate)
        assertThat(parseCreatedAt("9-6-19"))
                .isEqualTo(expectedDate)
        assertThat(parseCreatedAt("9-06-2019"))
                .isEqualTo(expectedDate)
        assertThat(parseCreatedAt("09-06-2019"))
                .isEqualTo(expectedDate)
    }
}

private fun date(day: Int, month: Int, year: Int): Instant =
        LocalDate.of(year, month, day)
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
