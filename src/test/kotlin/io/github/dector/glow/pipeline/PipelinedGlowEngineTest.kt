package io.github.dector.glow.pipeline

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Pipeline engine with extensions")
internal class PipelinedGlowEngineTest {

    @Test
    @DisplayName("Execute empty pipeline")
    fun execute_withEmptyPipeline() {
        // Given
        val engine = PipelinedGlowEngine(
                pipeline = EmptyPipeline()
        )

        // When
        val result = engine.execute()

        // Then
        assertThat(result.isSuccess)
                .isTrue()
    }

    @Test
    @DisplayName("Execute empty pipeline that throws error")
    fun execute_withEmptyPipelineThatThrows() {
        // Given
        val engine = PipelinedGlowEngine(
                pipeline = ErrorPipeline()
        )

        // When
        val result = engine.execute()

        // Then
        assertThat(result.isSuccess)
                .isFalse()
    }
}

private class EmptyPipeline : GlowPipeline {

    override fun execute() {}
}

private class ErrorPipeline : GlowPipeline {

    override fun execute() {
        throw object : Throwable() {}
    }
}