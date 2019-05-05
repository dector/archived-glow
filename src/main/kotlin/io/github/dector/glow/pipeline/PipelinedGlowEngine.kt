package io.github.dector.glow.pipeline

import io.github.dector.glow.core.GlowExecutionResult
import io.github.dector.glow.core.components.GlowEngine


class PipelinedGlowEngine(
        private val pipeline: GlowPipeline
) : GlowEngine {

    override fun execute(): GlowExecutionResult {
        try {
            pipeline.execute()
            return GlowExecutionResult.Success
        } catch (e: Throwable) {
            return GlowExecutionResult.Fail(e)
        }
    }
}

interface GlowPipeline {
    fun execute()
}