package io.github.dector.glow.v2.pipeline

import com.google.common.truth.Truth.assertThat
import io.github.dector.ext.truth.isInstanceOf
import io.github.dector.glow.v2.core.GlowExecutionResult
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
        assertThat(result)
                .isInstanceOf<GlowExecutionResult.Success>()
    }

    @Test
    @DisplayName("Execute empty pipeline that throws error")
    fun execute_withEmptyPipelineThatThrows() {
        // Given
        val engine = PipelinedGlowEngine(
                pipeline = ErrorPipeline()
        )

        // When
        val result = engine.execute() as? GlowExecutionResult.Fail

        // Then
        assertThat(result)
                .isNotNull()
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