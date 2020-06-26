package io.github.dector.glow.core.components

import io.github.dector.glow.logger.logger

class GlowEngine(
    private vararg val pipelines: GlowPipeline
) {

    fun execute(): ExecutionResult {
        val result = runCatching {
            pipelines.forEach {
                logger().debug("Executing plugin: ${it::class.simpleName}")
                it.execute()
            }
        }

        return if (result.isSuccess)
            ExecutionResult.Success
        else ExecutionResult.Fail(result.exceptionOrNull()!!)
    }

    sealed class ExecutionResult {
        object Success : ExecutionResult()
        data class Fail(val error: Throwable) : ExecutionResult()
    }
}

interface GlowPipeline {
    fun execute()
}
