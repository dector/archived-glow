package io.github.dector.glow.pipeline

import io.github.dector.glow.core.components.GlowEngine


class PipelinedGlowEngine(
        private val pipeline: GlowPipeline
) : GlowEngine {

    override fun execute(): Result<Unit> {
        try {
            pipeline.execute()
            return Result.success(Unit)
        } catch (e: Throwable) {
            return Result.failure(e)
        }
    }
}

interface GlowPipeline {
    fun execute()
}