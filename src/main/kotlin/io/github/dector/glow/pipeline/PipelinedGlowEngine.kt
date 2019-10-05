package io.github.dector.glow.pipeline

import arrow.core.Either
import io.github.dector.glow.core.components.GlowEngine


class PipelinedGlowEngine(
        private val pipeline: GlowPipeline
) : GlowEngine {

    override fun execute(): Either<Throwable, Unit> = try {
        pipeline.execute()
        Either.right(Unit)
    } catch (e: Throwable) {
        Either.left(e)
    }
}

interface GlowPipeline {
    fun execute()
}
