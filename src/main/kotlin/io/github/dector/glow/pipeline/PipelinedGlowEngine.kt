package io.github.dector.glow.pipeline

import io.github.dector.glow.core.components.GlowEngine
import io.github.dector.glow.core.components.GlowEngine.ExecutionResult.Fail
import io.github.dector.glow.core.components.GlowEngine.ExecutionResult.Success


class PipelinedGlowEngine(
    private val pipeline: GlowPipeline
) : GlowEngine {

    override fun execute() = run {
        val result = runCatching { pipeline.execute() }

        if (result.isSuccess)
            Success
        else Fail(result.exceptionOrNull()!!)
    }
}

interface GlowPipeline {
    fun execute()
}
