package io.github.dector.glow.v2.pipeline

import com.google.common.truth.Truth.assertThat
import io.github.dector.glow.v2.core.GlowExecutionResult
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Pipeline engine with extensions")
internal class PipelinedGlowEngineTest {

    @Test
    @DisplayName("Execute empty pipeline")
    fun execute_withEmptyPipeline() {
        // Given
        val engine = PipelinedGlowEngine()

        // When
        val result = engine.execute()

        // Then
        assertThat(result)
                .isEqualTo(GlowExecutionResult.Success)
    }
}