package io.github.dector.glow.core.parser

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class UtilsKtTest {

    @Test
    @DisplayName("parseCreatedAt()")
    fun parseCreatedAt_suit() {
        assertThat(parseCreatedAt(null))
                .isEqualTo(Instant.MIN)
        assertThat(parseCreatedAt(null) { Instant.MAX })
                .isEqualTo(Instant.MAX)
        assertThat(parseCreatedAt("9-06-2019"))
                .isEqualTo(date(9, 6, 2019))
        assertThat(parseCreatedAt("9-06-19"))
                .isEqualTo(date(9, 6, 2019))
        assertThat(parseCreatedAt("9-6-19"))
                .isEqualTo(date(9, 6, 2019))
        assertThat(parseCreatedAt("9-06-2019"))
                .isEqualTo(date(9, 6, 2019))
        assertThat(parseCreatedAt("09-06-2019"))
                .isEqualTo(date(9, 6, 2019))
        assertThat(parseCreatedAt("32-06-19"))
                .isEqualTo(Instant.MIN)
    }
}

private fun date(day: Int, month: Int, year: Int): Instant =
        LocalDate.of(year, month, day)
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
