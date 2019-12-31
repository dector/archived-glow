package io.github.dector.glow.core.components

interface GlowEngine {

    fun execute(): ExecutionResult

    sealed class ExecutionResult {
        object Success : ExecutionResult()
        data class Fail(val error: Throwable) : ExecutionResult()
    }
}
