package io.github.dector.glow.engine

import io.github.dector.glow.logger.logger

class GlowEngine(
    private vararg val pipelines: GlowPipeline
) {

    fun execute() {
        pipelines.forEach {
            logger().debug("[Indexing] plugin: ${it::class.simpleName}")
            it.onIndex()
        }

        pipelines.forEach {
            logger().debug("[Executing] plugin: ${it::class.simpleName}")
            it.onExecute()
        }
    }

    sealed class ExecutionResult {
        object Success : ExecutionResult()
        data class Fail(val error: Throwable) : ExecutionResult()
    }
}

interface GlowPipeline {
    fun onIndex() {}
    fun onExecute()
}
