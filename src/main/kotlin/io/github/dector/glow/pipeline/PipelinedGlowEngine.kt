package io.github.dector.glow.pipeline

import io.github.dector.glow.core.components.GlowEngine


class PipelinedGlowEngine(
        private val pipeline: GlowPipeline
) : GlowEngine {

    override fun execute(): Result<Unit> = try {
        pipeline.execute()
        Result.success(Unit)
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

interface GlowPipeline {
    fun execute()
}